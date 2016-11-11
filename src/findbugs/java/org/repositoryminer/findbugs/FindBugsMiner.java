package org.repositoryminer.findbugs;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Reference;
import org.repositoryminer.scm.ISCM;

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
	
	private List<Reference> references;
	private String repositoryId;
	private String repoTempPath;
	private ISCM scm;
	private Map<String, Commit> commitsMap;
	private FindBugsPersistence persistence;
	
	public FindBugsMiner() {
		this.persistence = new FindBugsPersistence();
	}
	
	public void mine(Priority bugsPriority, Effort effort) {
		FindBugsExecutor findbugs = new FindBugsExecutor(repoTempPath, 
				prioritiesMap.getOrDefault(bugsPriority, DEFAULT_PRIORITY),
				effortsMap.getOrDefault(effort, DEFAULT_EFFORT),
				userPrefsEffortMap.getOrDefault(effort, DEFAULT_USER_PREFS_EFFORT));
		
		for (Reference ref : references) {
			String commitId = ref.getCommits().get(0);
			scm.checkout(commitId);
			
			List<ReportedBug> reportedBugs = null;
			try {
				reportedBugs = findbugs.execute();
			} catch (IllegalStateException | IOException | InterruptedException e) {
				continue;
			}
			
			processBus(commitsMap.get(commitId), ref, reportedBugs);
		}
	}

	private void processBus(Commit commit, Reference ref, List<ReportedBug> reportedBugs) {
		Document doc = new Document();
		doc.append("reference_name", ref.getName());
		doc.append("reference_type", ref.getType().toString());
		doc.append("commit", commit.getId());
		doc.append("commit_date", commit.getCommitDate());
		doc.append("repository", new ObjectId(repositoryId));
		doc.append("bug_collection", ReportedBug.toDocumentList(reportedBugs));
		
		persistence.insert(doc);
	}

	public void setReferences(List<Reference> references) {
		this.references = references;
	}

	public void setRepository(String repositoryId, String repoTempPath) {
		this.repositoryId = repositoryId;
		this.repoTempPath = repoTempPath;
	}

	public void setScm(ISCM scm) {
		this.scm = scm;
	}
	
	public void setCommitsMap(Map<String, Commit> commitsMap) {
		this.commitsMap = commitsMap;
	}
	
}