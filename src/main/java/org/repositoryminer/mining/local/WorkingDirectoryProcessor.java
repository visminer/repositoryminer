package org.repositoryminer.mining.local;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.repositoryminer.listener.mining.IMiningListener;
import org.repositoryminer.model.Diff;
import org.repositoryminer.model.Reference;
import org.repositoryminer.model.WorkingDirectory;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.handler.WorkingDirectoryDocumentHandler;
import org.repositoryminer.scm.DiffType;

import com.mongodb.client.model.Projections;

public class WorkingDirectoryProcessor {

	private static final int COMMIT_RANGE = 1000;

	private CommitDocumentHandler commitHandler;
	private ReferenceDocumentHandler referenceHandler;
	private WorkingDirectoryDocumentHandler wdHandler;
	
	private Set<String> visitedCommits;
	private String repositoryId; 
	private List<Reference> references;
	private WorkingDirectory workingDirectory;
	private IMiningListener miningListener;
	
	public WorkingDirectoryProcessor() {
		commitHandler = new CommitDocumentHandler();
		referenceHandler = new ReferenceDocumentHandler();
		wdHandler = new WorkingDirectoryDocumentHandler();
	}

	public void setVisitedCommits(Set<String> visitedCommits) {
		this.visitedCommits = visitedCommits;
	}

	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}

	public void setReferences(List<Reference> references) {
		this.references = references;
	}

	public void setMiningListener(IMiningListener miningListener) {
		this.miningListener = miningListener;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void processWorkingDirectories() {
		if (visitedCommits == null) {
			visitedCommits = new HashSet<String>();
		}
		
		for (Reference ref : references) {
			workingDirectory = new WorkingDirectory(repositoryId);
			Document refDoc = referenceHandler.findById(ref.getId(), Projections.include("commits"));

			List<String> commits = (List<String>) refDoc.get("commits");

			miningListener.notifyWorkingDirectoriesMiningStart(ref.getName(), ref.getType(), commits.size());
			
			int end = commits.size();
			int begin = Math.max(end - COMMIT_RANGE, 0);
			int acceptedCommits = 0;
			
			while (begin > 0) {
				acceptedCommits += processCommits(new ArrayList(commits.subList(begin, end)), ref);
				end = begin;
				begin = Math.max(end - COMMIT_RANGE, 0);
			}

			acceptedCommits += processCommits(new ArrayList(commits.subList(begin, end)), ref);
			miningListener.notifyWorkingDirectoriesMiningEnd(ref.getName(), ref.getType(), acceptedCommits);
		}
	}

	@SuppressWarnings("unchecked")
	private int processCommits(List<String> commits, Reference ref) {
		// not selects already processed commits
		List<String> newCommits = new ArrayList<String>();
		String prevCommit = null;
		
		for (int i = commits.size() - 1; i >= 0; i--) {
			if (visitedCommits.contains(commits.get(i))) {
				prevCommit = commits.get(i);
			} else {
				newCommits.add(commits.get(i));
			}
		}
		
		if (newCommits.size() == 0) {
			return 0;
		}
		
		if (prevCommit != null) {
			workingDirectory = WorkingDirectory.parseDocument(wdHandler.findById(prevCommit));
		}
		
		List<Document> wdDocs = new ArrayList<Document>();
		for (Document doc : commitHandler.findByIdColl(repositoryId, newCommits, Projections.include("diffs"))) {
			String commitId = doc.get("_id").toString();
			miningListener.notifyWorkingDirectoriesMiningProgress(ref.getName(), ref.getType(), commitId);
			
			visitedCommits.add(commitId);
			workingDirectory.setId(commitId);
			
			processDiff(Diff.parseDocuments((List<Document>) doc.get("diffs")));
			wdDocs.add(workingDirectory.toDocument());
		}
		
		wdHandler.insertMany(wdDocs);
		return wdDocs.size();
	}

	private void processDiff(List<Diff> diffs) {
		for (Diff d : diffs) {
			if (d.getType() == DiffType.ADD || d.getType() == DiffType.COPY) {
				workingDirectory.getFiles().put(d.getPath(), workingDirectory.getId());
			} else if (d.getType() == DiffType.DELETE) {
				workingDirectory.getFiles().remove(d.getPath());
			} else if(d.getType() == DiffType.RENAME) {
				workingDirectory.getFiles().remove(d.getOldPath());
				workingDirectory.getFiles().put(d.getPath(), workingDirectory.getId());
			}
		}
	}

}