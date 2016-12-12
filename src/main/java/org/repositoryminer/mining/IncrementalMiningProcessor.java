package org.repositoryminer.mining;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.Document;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Contributor;
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

public class IncrementalMiningProcessor {

	private ISCM scm;
	private IssueExtractor messageAnalyzer;
	private RepositoryMiner repositoryMiner;
	private List<Reference> selectedReferences;
	private Set<String> visitedCommits; // stores all the commits before starts the incremental analysis
	private Set<String> newerCommits; // stores the new commits found in the incremental analysis
	private Set<Contributor> contributors;

	private void updateReferences(String repositoryId) {
		selectedReferences = new ArrayList<Reference>();
		contributors = new HashSet<Contributor>();
		messageAnalyzer = new IssueExtractor();
		newerCommits = new HashSet<String>();
		
		ReferenceDocumentHandler refDocumentHandler = new ReferenceDocumentHandler();
		Set<String> commitsToSkip = new HashSet<String>(visitedCommits);
		
		for (Reference ref : scm.getReferences()) {
			Entry<String, ReferenceType> entry = new AbstractMap.SimpleEntry<String, ReferenceType>(ref.getName(),
                    ref.getType());

            if (!repositoryMiner.getReferences().contains(entry)) {
                continue;
            }
            
            ref.setRepository(repositoryId);
            Document refDoc = refDocumentHandler.findByPath(ref.getPath(), repositoryId, Projections.include("_id"));
            
            if (refDoc == null) {
                ref.setCommits(scm.getReferenceCommits(ref.getPath(), ref.getType()));
                refDoc = ref.toDocument();
                refDocumentHandler.insert(refDoc);
            } else {
            	refDocumentHandler.updateOnlyCommits(refDoc.getObjectId("_id").toString(), scm.getReferenceCommits(ref.getPath(), ref.getType()));
            }
            
            ref.setId(refDoc.getObjectId("_id").toString());
            updateCommits(ref, commitsToSkip);
            
            ref.setCommits(null);
            selectedReferences.add(ref);
		}
	}
	
	private void updateCommits(Reference reference, Set<String> commitsToSkip) {
		CommitDocumentHandler documentHandler = new CommitDocumentHandler();
		repositoryMiner.getMiningListener().initCommitsMining();
		
		int skip = 0;
		List<Commit> commits = scm.getCommits(skip, repositoryMiner.getCommitCount(), reference, commitsToSkip);
		
		while (commits.size() > 0) {
			List<Document> commitsDoc = new ArrayList<Document>();

			for (Commit commit : commits) {
				commit.setRepository(reference.getRepository());
				commit.setIssueReferences(messageAnalyzer.analyzeMessage(commit.getMessage()));

				contributors.add(commit.getCommitter());
				commitsDoc.add(commit.toDocument());

				commitsToSkip.add(commit.getId());
				newerCommits.add(commit.getId());
			}

			documentHandler.insertMany(commitsDoc);
			skip += repositoryMiner.getCommitCount();
			commits = scm.getCommits(skip, repositoryMiner.getCommitCount(), reference, commitsToSkip);
		}
	}
	
	public void mine(RepositoryMiner repositoryMiner) throws IOException {
		this.repositoryMiner = repositoryMiner;
		
		RepositoryDocumentHandler repoHandler = new RepositoryDocumentHandler();
		Repository repository = Repository.parseDocument(repoHandler.findByName(repositoryMiner.getName()));
		
		String tempRepo = FileUtils.copyFolderToTmp(repository.getPath(), repository.getName());
		
		scm = SCMFactory.getSCM(repositoryMiner.getScm());
        scm.open(tempRepo);
        
        CommitDocumentHandler commitDocHandler = new CommitDocumentHandler();
        visitedCommits = new HashSet<String>();
        List<Document> commitsDoc = commitDocHandler.findByRepository(repository.getId(), Projections.include("_id"));
        for (Document d : commitsDoc) {
        	visitedCommits.add(d.getString("_id"));
        }
        
        updateReferences(repository.getId());
        
        for (Contributor c : repository.getContributors()) {
        	contributors.add(c);
        }
        repoHandler.updateOnlyContributors(repository.getId(), Contributor.toDocumentList(contributors));
        
        saveWorkingDirectories(repository.getId());
        calculateAndDetect(tempRepo, repository.getId());
        
        scm.close();
        FileUtils.deleteFolder(tempRepo);
	}
	
	private void saveWorkingDirectories(String repositoryId) {
		WorkingDirectoryProcessor wdProcessor = new WorkingDirectoryProcessor();
		wdProcessor.setListener(repositoryMiner.getMiningListener());
		wdProcessor.setReferences(selectedReferences);
		wdProcessor.setVisitedCommits(visitedCommits);
		wdProcessor.setRepositoryId(repositoryId);
		wdProcessor.processWorkingDirectories();
	}
	
	// Performs both the calculation (metrics) and detections (smells/debts) on the targeted project.
	private void calculateAndDetect(String tempRepo, String repositoryId) throws IOException {
		if (!repositoryMiner.shouldProcessCommits() && !repositoryMiner.shouldProcessReferences())
			return;

		if (selectedReferences.size() == 0)
			return;

		if (repositoryMiner.shouldProcessCommits()) {
			CommitProcessor commitProcessor = new CommitProcessor();
			commitProcessor.setReferences(selectedReferences);
			commitProcessor.setSCM(scm);
			commitProcessor.setRepositoryMiner(repositoryMiner);
			commitProcessor.setVisitedCommits(visitedCommits);
			commitProcessor.setRepositoryData(repositoryId, tempRepo);
			commitProcessor.start();
		}

		if (repositoryMiner.shouldProcessReferences()) {
			List<String> validSnapshots = new ArrayList<String>();
			for (String hash : repositoryMiner.getSnapshots()) {
				if (visitedCommits.contains(hash) || newerCommits.contains(hash)) {
					validSnapshots.add(hash);
				}
			}

			SnapshotProcessor snapshotProcessor = new SnapshotProcessor();
			snapshotProcessor.setReferences(selectedReferences);
			snapshotProcessor.setSnapshots(validSnapshots);
			snapshotProcessor.setRepositoryData(repositoryId, tempRepo);
			snapshotProcessor.setRepositoryMiner(repositoryMiner);
			snapshotProcessor.setSCM(scm);
			snapshotProcessor.startUpdate();
		}
	}
	
}