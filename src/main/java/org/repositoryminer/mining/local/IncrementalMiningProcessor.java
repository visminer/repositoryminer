package org.repositoryminer.mining.local;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.Document;
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
				refDocumentHandler.updateOnlyCommits(refDoc.getObjectId("_id").toString(),
						scm.getReferenceCommits(ref.getPath(), ref.getType()));
			}

			ref.setId(refDoc.getObjectId("_id").toString());
			ref.setCommits(null);
			selectedReferences.add(ref);
		}
	}

	private void updateCommits(Reference reference, Set<String> commitsToSkip) {
		CommitDocumentHandler documentHandler = new CommitDocumentHandler();

		int skip = 0;
		List<Commit> commits = scm.getCommits(skip, COMMITS_RANGE, reference, commitsToSkip);

		while (commits.size() > 0) {
			List<Document> commitsDoc = new ArrayList<Document>();

			for (Commit commit : commits) {
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
	}

	public void mine(RepositoryMiner repositoryMiner) throws IOException {
		this.repositoryMiner = repositoryMiner;

		RepositoryDocumentHandler repoHandler = new RepositoryDocumentHandler();
		Repository repository = Repository.parseDocument(repoHandler.findByName(repositoryMiner.getName()));

		String tempRepo = FileUtils.copyFolderToTmp(repository.getPath(), repository.getName());

		scm = SCMFactory.getSCM(repositoryMiner.getScm());
		scm.open(tempRepo);

		loadAllCommits(repository.getId());

		updateReferences(repository.getId());
		
		newCommits = new ArrayList<String>();
		Set<String> commitsToSkip = new HashSet<String>(processedCommits);
		for (Reference ref : selectedReferences) {
			updateCommits(ref, commitsToSkip);
		}

		updateWorkingDirectories(repository.getId());
		calculateAndDetect(tempRepo, repository.getId());
		
		scm.close();
		FileUtils.deleteFolder(tempRepo);
	}

	private void updateWorkingDirectories(String repositoryId) {
		WorkingDirectoryProcessor wdProcessor = new WorkingDirectoryProcessor();
		wdProcessor.setReferences(selectedReferences);
		wdProcessor.setVisitedCommits(processedCommits);
		wdProcessor.setRepositoryId(repositoryId);
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
			CodeAnalysisProcessor codeAnalysisProcessor = new CodeAnalysisProcessor();
			codeAnalysisProcessor.setSCM(scm);
			codeAnalysisProcessor.setRepositoryMiner(repositoryMiner);
			codeAnalysisProcessor.setRepositoryData(repositoryId, tempRepo);
			codeAnalysisProcessor.startIncrementalAnalysis(newCommits);
		}

	}

}