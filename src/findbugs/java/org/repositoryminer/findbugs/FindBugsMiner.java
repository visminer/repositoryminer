package org.repositoryminer.findbugs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.findbugs.configuration.Effort;
import org.repositoryminer.findbugs.configuration.Priority;
import org.repositoryminer.findbugs.model.ReportedBug;
import org.repositoryminer.findbugs.persistence.FindBugsDocumentHandler;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Reference;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.handler.WorkingTreeDocumentHandler;
import org.repositoryminer.scm.ReferenceType;
import org.repositoryminer.utility.StringUtils;

import com.mongodb.client.model.Projections;

import edu.umd.cs.findbugs.FindBugs;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.config.AnalysisFeatureSetting;
import edu.umd.cs.findbugs.config.UserPreferences;

public class FindBugsMiner {

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

	private final FindBugsDocumentHandler findBugsPersist = new FindBugsDocumentHandler();
	private final CommitDocumentHandler commitPersist = new CommitDocumentHandler();
	private final ReferenceDocumentHandler refPersist = new ReferenceDocumentHandler();
	private final WorkingTreeDocumentHandler wtPersist = new WorkingTreeDocumentHandler();

	private String repositoryId;
	private Priority priority;
	private Effort effort;

	private Set<String> analysisClasspath = new HashSet<String>();
	private Set<String> auxiliaryClasspath = new HashSet<String>();
	private Set<String> sourceDirectories = new HashSet<String>();
	
	public FindBugsMiner(String repositoryId) {
		this.repositoryId = repositoryId;
	}

	public void execute(String hash) throws IllegalStateException, IOException, InterruptedException {
		persistAnalysis(hash, null);
	}

	public void execute(String name, ReferenceType type)
			throws IllegalStateException, IOException, InterruptedException {
		Document refDoc = refPersist.findByNameAndType(name, type, repositoryId, Projections.slice("commits", 1));
		Reference reference = Reference.parseDocument(refDoc);

		String commitId = reference.getCommits().get(0);
		persistAnalysis(commitId, reference);
	}

	private void persistAnalysis(String commitId, Reference ref)
			throws IllegalStateException, IOException, InterruptedException {
		Commit commit = Commit.parseDocument(commitPersist.findById(commitId, Projections.include("commit_date")));

		configureFindBugs();
		Map<String, List<ReportedBug>> reportedBugs = findBugsExecutor.execute();

		List<String> files = createFilesList(commitId);

		List<Document> documents = new ArrayList<Document>(reportedBugs.size());
		for (Entry<String, List<ReportedBug>> bug : reportedBugs.entrySet()) {
			Document doc = new Document();

			if (ref != null) {
				doc.append("reference_name", ref.getName());
				doc.append("reference_type", ref.getType().toString());
			}

			doc.append("commit", commit.getId());
			doc.append("commit_date", commit.getCommitDate());
			doc.append("repository", new ObjectId(repositoryId));

			String filename = null;
			for (String file : files) {
				if (file.endsWith(bug.getKey())) {
					filename = file;
					break;
				}
			}

			doc.append("filename", filename);
			doc.append("filehash", StringUtils.encodeToCRC32(filename));
			doc.append("bugs", ReportedBug.toDocumentList(bug.getValue()));

			documents.add(doc);
		}

		findBugsPersist.insertMany(documents);
	}

	@SuppressWarnings("unchecked")
	// create a list with the files names in a checkout
	private List<String> createFilesList(String commitId) {
		List<Document> filesDoc = (List<Document>) wtPersist.findById(commitId, Projections.include("files.file"))
				.get("files");
		List<String> files = new ArrayList<String>(filesDoc.size());

		for (Document file : filesDoc) {
			files.add(file.getString("file"));
		}

		return files;
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