package org.repositoryminer.findbugs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.findbugs.configuration.Effort;
import org.repositoryminer.findbugs.configuration.Priority;
import org.repositoryminer.findbugs.model.ReportedBug;
import org.repositoryminer.findbugs.persistence.FindBugsDocumentHandler;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Reference;
import org.repositoryminer.model.Repository;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.handler.RepositoryDocumentHandler;
import org.repositoryminer.scm.ISCM;
import org.repositoryminer.scm.ReferenceType;
import org.repositoryminer.scm.SCMFactory;
import org.repositoryminer.utility.FileUtils;

import com.mongodb.client.model.Projections;

import edu.umd.cs.findbugs.FindBugs;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.config.AnalysisFeatureSetting;
import edu.umd.cs.findbugs.config.UserPreferences;

public class FindBugsMiner {

	private static Map<Priority, Integer> prioritiesMap = new HashMap<Priority, Integer>();
	private static Map<Effort, AnalysisFeatureSetting[]> effortsMap = new HashMap<Effort, AnalysisFeatureSetting[]>();
	private static Map<Effort, String> userPrefsEffortMap = new HashMap<Effort, String>();

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

	private Repository repository;
	private ISCM scm;
	private String tmpRepository;
	private FindBugsExecutor findBugsExecutor;

	private FindBugsDocumentHandler findBugsPersist;
	private CommitDocumentHandler commitPersist;
	private ReferenceDocumentHandler refPersist;

	private Priority priority = Priority.NORMAL;
	private Effort effort = Effort.DEFAULT;

	public FindBugsMiner(Repository repository) {
		this.repository = repository;
	}

	public FindBugsMiner(String repositoryId) {
		RepositoryDocumentHandler repoHandler = new RepositoryDocumentHandler();
		this.repository = Repository.parseDocument(repoHandler.findById(repositoryId, Projections.include("scm", "path")));
	}

	public void findBugs(String hash) throws IllegalStateException, IOException, InterruptedException {
		persistAnalysis(hash);
	}

	public void findBugs(String name, ReferenceType type)
			throws IllegalStateException, IOException, InterruptedException {
		Document refDoc = refPersist.findByNameAndType(name, type, repository.getId(), Projections.slice("commits", 1));
		Reference reference = Reference.parseDocument(refDoc);

		String commitId = reference.getCommits().get(0);
		persistAnalysis(commitId);
	}

	public void configure() throws IOException {
		tmpRepository = FileUtils.copyFolderToTmp(repository.getPath(), repository.getId());
		findBugsExecutor = new FindBugsExecutor(tmpRepository);

		findBugsPersist = new FindBugsDocumentHandler();
		commitPersist = new CommitDocumentHandler();
		refPersist = new ReferenceDocumentHandler();

		scm = SCMFactory.getSCM(repository.getScm());
		scm.open(tmpRepository);
	}

	public void checkout(String ref) {
		scm.checkout(ref);
	}

	public void checkout(String reference, ReferenceType type) {
		List<Reference> references = scm.getReferences();
		for (Reference r : references) {
			if (r.getName().equals(reference) && r.getType().equals(type)) {
				scm.checkout(r.getPath());
				break;
			}
		}
	}

	public void dispose() throws IOException {
		scm.close();
		FileUtils.deleteFolder(tmpRepository);
	}

	public void setEffort(Effort effort) {
		this.effort = effort;
	}

	public void setBugPriority(Priority priority) {
		this.priority = priority;
	}

	private void persistAnalysis(String commitId)
			throws IllegalStateException, IOException, InterruptedException {
		Commit commit = Commit.parseDocument(commitPersist.findById(commitId, Projections.include("commit_date")));

		configureFindBugs();
		Map<String, List<ReportedBug>> reportedBugs = findBugsExecutor.execute();

		List<Document> documents = new ArrayList<Document>(reportedBugs.size());
		for (Entry<String, List<ReportedBug>> bug : reportedBugs.entrySet()) {
			Document doc = new Document();
			doc.append("commit", commit.getId());
			doc.append("commit_date", commit.getCommitDate());
			doc.append("repository", new ObjectId(repository.getId()));
			doc.append("filename", bug.getKey());
			doc.append("bugs", ReportedBug.toDocumentList(bug.getValue()));
			
			documents.add(doc);
		}
		
		findBugsPersist.insertMany(documents);
	}

	private void configureFindBugs() {
		findBugsExecutor.setBugPriority(prioritiesMap.getOrDefault(priority, DEFAULT_PRIORITY));
		findBugsExecutor.setEffort(effortsMap.getOrDefault(effort, DEFAULT_EFFORT));
		findBugsExecutor.setUserPrefsEffort(userPrefsEffortMap.getOrDefault(effort, DEFAULT_USER_PREFS_EFFORT));
	}

}