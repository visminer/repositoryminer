package org.repositoryminer.findbugs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.domain.Commit;
import org.repositoryminer.domain.Reference;
import org.repositoryminer.domain.ReferenceType;
import org.repositoryminer.domain.Repository;
import org.repositoryminer.findbugs.configuration.Effort;
import org.repositoryminer.findbugs.configuration.Priority;
import org.repositoryminer.findbugs.model.ReportedBug;
import org.repositoryminer.findbugs.persistence.FindBugsDAO;
import org.repositoryminer.persistence.dao.CommitDAO;
import org.repositoryminer.persistence.dao.ReferenceDAO;
import org.repositoryminer.persistence.dao.RepositoryDAO;
import org.repositoryminer.scm.ISCM;
import org.repositoryminer.scm.SCMFactory;
import org.repositoryminer.util.HashingUtils;
import org.repositoryminer.util.RMFileUtils;

import com.mongodb.client.model.Projections;

import edu.umd.cs.findbugs.FindBugs;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.config.AnalysisFeatureSetting;
import edu.umd.cs.findbugs.config.UserPreferences;

public class FindBugsMiner {

	private static final String[] EXTENSION_FILE_FILTER = { "java" };

	private static final Map<Priority, Integer> prioritiesMap = new HashMap<Priority, Integer>();
	private static final Map<Effort, AnalysisFeatureSetting[]> effortsMap = new HashMap<Effort, AnalysisFeatureSetting[]>();
	private static final Map<Effort, String> userPrefsEffortMap = new HashMap<Effort, String>();

	static {
		prioritiesMap.put(Priority.LOW, Priorities.IGNORE_PRIORITY);
		prioritiesMap.put(Priority.NORMAL, Priorities.NORMAL_PRIORITY);
		prioritiesMap.put(Priority.HIGH, Priorities.HIGH_PRIORITY);

		effortsMap.put(Effort.MIN, FindBugs.MIN_EFFORT);
		effortsMap.put(Effort.DEFAULT, FindBugs.DEFAULT_EFFORT);
		effortsMap.put(Effort.MAX, FindBugs.MAX_EFFORT);

		userPrefsEffortMap.put(Effort.MIN, UserPreferences.EFFORT_MIN);
		userPrefsEffortMap.put(Effort.DEFAULT, UserPreferences.EFFORT_DEFAULT);
		userPrefsEffortMap.put(Effort.MAX, UserPreferences.EFFORT_MAX);
	}

	private static final int DEFAULT_PRIORITY = Priorities.NORMAL_PRIORITY;
	private static final AnalysisFeatureSetting[] DEFAULT_EFFORT = FindBugs.DEFAULT_EFFORT;
	private static final String DEFAULT_USER_PREFS_EFFORT = UserPreferences.EFFORT_DEFAULT;

	private final FindBugsExecutor findBugsExecutor = new FindBugsExecutor();

	private final FindBugsDAO findBugsPersist = new FindBugsDAO();
	private final CommitDAO commitPersist = new CommitDAO();
	private final ReferenceDAO refPersist = new ReferenceDAO();

	private ISCM scm;
	private String tempRepository;
	private Repository repository;

	private Priority priority;
	private Effort effort;

	private Set<String> analysisClasspath = new HashSet<String>();
	private Set<String> auxiliaryClasspath = new HashSet<String>();
	private Set<String> sourceDirectories = new HashSet<String>();

	public FindBugsMiner(String repositoryId) {
		this.repository = Repository
				.parseDocument(new RepositoryDAO().findById(repositoryId, Projections.include("path", "name", "scm")));
	}

	public FindBugsMiner(Repository repository) {
		this.repository = repository;
	}

	public void prepare() throws IOException {
		File repositoryFolder = new File(repository.getPath());
		tempRepository = RMFileUtils.copyFolderToTmp(repositoryFolder.getAbsolutePath(), repository.getName());

		scm = SCMFactory.getSCM(repository.getScm());
		scm.open(tempRepository);
	}

	public void dispose() throws IOException {
		scm.close();
		RMFileUtils.deleteFolder(tempRepository);
	}

	public void execute(String hash) throws IllegalStateException, IOException, InterruptedException {
		persistAnalysis(hash, null);
	}

	public void execute(String name, ReferenceType type)
			throws IllegalStateException, IOException, InterruptedException {
		Document refDoc = refPersist.findByNameAndType(name, type, repository.getId(), Projections.slice("commits", 1));
		Reference reference = Reference.parseDocument(refDoc);

		String commitId = reference.getCommits().get(0);
		persistAnalysis(commitId, reference);
	}

	private void persistAnalysis(String commitId, Reference ref)
			throws IllegalStateException, IOException, InterruptedException {
		Commit commit = Commit.parseDocument(commitPersist.findById(commitId, Projections.include("commit_date")));

		scm.checkout(commitId);

		configureFindBugs();
		Map<String, List<ReportedBug>> reportedBugs = findBugsExecutor.execute();

		List<String> files = getFiles(repository.getPath());

		List<Document> documents = new ArrayList<Document>(reportedBugs.size());
		for (Entry<String, List<ReportedBug>> bug : reportedBugs.entrySet()) {
			Document doc = new Document();

			if (ref != null) {
				doc.append("reference", ref.getPath());
			}

			doc.append("commit", commit.getId());
			doc.append("commit_date", commit.getCommitDate());
			doc.append("repository", new ObjectId(repository.getId()));

			String filename = null;
			for (String file : files) {
				if (file.endsWith(bug.getKey())) {
					filename = file;
					break;
				}
			}

			doc.append("filename", filename);
			doc.append("filehash", HashingUtils.encodeToCRC32(filename));
			doc.append("bugs", ReportedBug.toDocumentList(bug.getValue()));

			documents.add(doc);
		}

		findBugsPersist.insertMany(documents);
	}

	private List<String> getFiles(String dir) {
		Collection<File> files = FileUtils.listFiles(new File(dir), EXTENSION_FILE_FILTER, true);
		List<String> filepaths = new ArrayList<String>(files.size());

		for (File f : files) {
			filepaths.add(FilenameUtils.normalize(f.getAbsolutePath().substring(dir.length() + 1), true));
		}

		return filepaths;
	}

	private void configureFindBugs() {
		findBugsExecutor.setBugPriority(prioritiesMap.getOrDefault(priority, DEFAULT_PRIORITY));
		findBugsExecutor.setEffort(effortsMap.getOrDefault(effort, DEFAULT_EFFORT));
		findBugsExecutor.setUserPrefsEffort(userPrefsEffortMap.getOrDefault(effort, DEFAULT_USER_PREFS_EFFORT));
		findBugsExecutor.setAnalysisClasspath(analysisClasspath);
		findBugsExecutor.setAuxiliaryClasspath(auxiliaryClasspath);
		findBugsExecutor.setSourceDirectories(sourceDirectories);
	}

	public void setEffort(Effort effort) {
		this.effort = effort;
	}

	public void setBugPriority(Priority priority) {
		this.priority = priority;
	}

	public boolean addAnalysisClassPath(String clsPath) {
		return analysisClasspath.add(clsPath);
	}

	public boolean addAuxiliaryClasspath(String clsPath) {
		return auxiliaryClasspath.add(clsPath);
	}

	public boolean addSourceDirectory(String srcDir) {
		return sourceDirectories.add(srcDir);
	}

}