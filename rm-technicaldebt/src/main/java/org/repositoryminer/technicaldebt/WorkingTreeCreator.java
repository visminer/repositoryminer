package org.repositoryminer.technicaldebt;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.repositoryminer.domain.Change;
import org.repositoryminer.domain.ChangeType;
import org.repositoryminer.persistence.dao.CommitDAO;
import org.repositoryminer.persistence.dao.ReferenceDAO;

import com.mongodb.client.model.Projections;

public class WorkingTreeCreator {

	private static final int COMMIT_RANGE = 1000;

	private CommitDAO commitDAO = new CommitDAO();
	private ReferenceDAO refDAO = new ReferenceDAO();
	private Map<String, String> workingTree;

	public Map<String, String> createFromReference(List<String> commits) {
		workingTree = new HashMap<String, String>();
		processWorkingTree(commits);
		return workingTree;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> createFromCommit(String commitId, String repositoryId) {
		workingTree = new HashMap<String, String>();
		
		Document refDoc = refDAO.findByCommit(repositoryId, commitId, Projections.include("commits"));
		List<String> commits = (List<String>) refDoc.get("commits");
		commits.subList(0, commits.indexOf(commitId)).clear();
		Collections.reverse(commits);
		
		processWorkingTree(commits);
		return workingTree;
	}
	
	private void processWorkingTree(List<String> commits) {
		int end = commits.size();
		int begin = Math.max(end - COMMIT_RANGE, 0);
		while (begin > 0) {
			processCommits(commits.subList(begin, end));
			end = begin;
			begin = Math.max(end - COMMIT_RANGE, 0);
		}
		processCommits(commits.subList(begin, end));
	}

	@SuppressWarnings("unchecked")
	private void processCommits(List<String> commits) {
		for (Document doc : commitDAO.findByIdList(commits, Projections.include("diffs"))) {
			String commitId = doc.getObjectId("_id").toHexString();
			for (Change d : Change.parseDocuments((List<Document>) doc.get("diffs")))
				if (d.getType() == ChangeType.ADD || d.getType() == ChangeType.COPY) {
					workingTree.put(d.getPath(), commitId);
				} else if (d.getType() == ChangeType.DELETE) {
					workingTree.remove(d.getPath());
				} else if (d.getType() == ChangeType.MOVE) {
					workingTree.remove(d.getOldPath());
					workingTree.put(d.getPath(), commitId);
				}
		}
	}
}