package org.repositoryminer.mining.local;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.Document;
import org.repositoryminer.listener.mining.IMiningListener;
import org.repositoryminer.mining.RepositoryMiner;
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

	private static final int COMMITS_RANGE = 3000;

	private RepositoryMiner repositoryMiner;
	private IMiningListener listener;

	private ISCM scm;
	private Set<String> processedCommits;
	private Set<Contributor> contributors;
	private IssueExtractor issueExtractor;
	private List<Reference> selectedReferences;
	private List<String> newCommits;

	private void updateReferences(String repositoryId) {
		contributors = new HashSet<Contributor>();
		issueExtractor = new IssueExtractor();
		selectedReferences = new ArrayList<Reference>();

		ReferenceDocumentHandler refDocumentHandler = new ReferenceDocumentHandler();
		List<Reference> references = scm.getReferences();

		listener.notifyReferencesMiningStart(references.size());
		Set<String> commitsToSkip = new HashSet<String>(processedCommits);

		for (Reference ref : references) {
			Entry<String, ReferenceType> entry = new AbstractMap.SimpleEntry<String, ReferenceType>(ref.getName(),
					ref.getType());

			if (!repositoryMiner.getReferences().contains(entry)) {
				continue;
			}

			listener.notifyReferencesMiningProgress(ref.getName(), ref.getType());

			ref.setRepository(repositoryId);
			Document refDoc = refDocumentHandler.findByPath(ref.getPath(), repositoryId, Projections.include("_id"));

			ref.setCommits(scm.getReferenceCommits(ref.getPath(), ref.getType()));
			
			if (refDoc == null) {
				refDoc = ref.toDocument();
				refDocumentHandler.insert(refDoc);
			} else {
				refDocumentHandler.updateOnlyCommits(refDoc.getObjectId("_id").toString(),
						ref.getCommits());
			}

			listener.notifyCommitsMiningStart(ref.getName(), ref.getType(), ref.getCommits().size());
			listener.notifyCommitsMiningEnd(ref.getName(), ref.getType(), updateCommits(ref, commitsToSkip));
			
			ref.setCommits(ref.getCommits().subList(0, 1)); // copy only the last commit in the reference
			ref.setId(refDoc.getObjectId("_id").toString());
			selectedReferences.add(ref);
		}

		listener.notifyReferencesMiningEnd(selectedReferences.size());
	}

	private int updateCommits(Reference reference, Set<String> commitsToSkip) {
		CommitDocumentHandler documentHandler = new CommitDocumentHandler();

		int skip = 0;
		int acceptedCommits = 0;
		List<Commit> commits = scm.getCommits(skip, COMMITS_RANGE, reference, commitsToSkip);

		while (commits.size() > 0) {
			List<Document> commitsDoc = new ArrayList<Document>();
			acceptedCommits += commits.size();

			for (Commit commit : commits) {
				listener.notifyCommitsMiningProgress(reference.getName(), reference.getType(), commit.getId());

				commit.setRepository(reference.getRepository());
				commit.setIssueReferences(issueExtractor.analyzeMessage(commit.getMessage()));

				contributors.add(commit.getCommitter());
				commitsDoc.add(commit.toDocument());

				commitsToSkip.add(commit.getId());
				newCommits.add(commit.getId());
			}

			documentHandler.insertMany(commitsDoc);
			skip += COMMITS_RANGE;
			commits = scm.getCommits(skip, COMMITS_RANGE, reference, commitsToSkip);
		}

		return acceptedCommits;
	}

	public void mine(RepositoryMiner repositoryMiner) throws IOException {
		this.repositoryMiner = repositoryMiner;
		listener = repositoryMiner.getMiningListener();
		newCommits = new ArrayList<String>();
		
		listener.notifyMiningStart(repositoryMiner.getName());

		RepositoryDocumentHandler repoHandler = new RepositoryDocumentHandler();
		Repository repository = Repository.parseDocument(repoHandler.findByName(repositoryMiner.getName()));

		String tempRepo = FileUtils.copyFolderToTmp(repository.getPath(), repository.getName());

		scm = SCMFactory.getSCM(repositoryMiner.getScm());
		scm.open(tempRepo);

		loadAllCommits(repository.getId());

		updateReferences(repository.getId());
		
		updateWorkingDirectories(repository.getId());
		
		calculateAndDetect(tempRepo, repository.getId());

		scm.close();
		FileUtils.deleteFolder(tempRepo);

		listener.notifyMiningEnd(repositoryMiner.getName());
	}

	private void updateWorkingDirectories(String repositoryId) {
		WorkingDirectoryProcessor wdProcessor = new WorkingDirectoryProcessor();
		wdProcessor.setReferences(selectedReferences);
		wdProcessor.setVisitedCommits(processedCommits);
		wdProcessor.setRepositoryId(repositoryId);
		wdProcessor.setMiningListener(listener);
		wdProcessor.processWorkingDirectories();
	}

	private void loadAllCommits(String repositoryId) {
		CommitDocumentHandler commitDocHandler = new CommitDocumentHandler();
		processedCommits = new HashSet<String>();

		List<Document> commitsDoc = commitDocHandler.findByRepository(repositoryId, Projections.include("_id"));

		for (Document d : commitsDoc) {
			processedCommits.add(d.getString("_id"));
		}
	}

	private void calculateAndDetect(String tempRepo, String repositoryId) throws IOException {
		if (!repositoryMiner.hasParsers() || selectedReferences.size() == 0) {
			return;
		}

		if (repositoryMiner.hasDirectCodeMetrics() || repositoryMiner.hasDirectCodeSmells()) {
			DirectCodeAnalysisProcessor processor = new DirectCodeAnalysisProcessor();
			processor.setSCM(scm);
			processor.setRepositoryMiner(repositoryMiner);
			processor.setRepositoryData(repositoryId, tempRepo);
			processor.setSelectedCommits(newCommits);
			processor.start();
		}

		if (repositoryMiner.hasIndirectCodeMetrics() || repositoryMiner.hasIndirectCodeSmells()) {
			List<String> validSnapshots = new ArrayList<String>();
			for (String hash : repositoryMiner.getSnapshots()) {
				if (processedCommits.contains(hash) || newCommits.contains(hash)) {
					validSnapshots.add(hash);
				}
			}

			IndirectCodeAnalysisProcessor processor = new IndirectCodeAnalysisProcessor();
			processor.setReferences(selectedReferences);
			processor.setSnapshots(validSnapshots);
			processor.setRepositoryData(repositoryId, tempRepo);
			processor.setRepositoryMiner(repositoryMiner);
			processor.setSCM(scm);
			processor.startIncrementalAnalysis();
		}

	}

}