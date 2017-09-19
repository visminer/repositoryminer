package org.repositoryminer.mining;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.PersonIdent;
import org.repositoryminer.model.Reference;
import org.repositoryminer.model.Repository;
import org.repositoryminer.persistence.dao.CommitDAO;
import org.repositoryminer.persistence.dao.ReferenceDAO;
import org.repositoryminer.persistence.dao.RepositoryDAO;
import org.repositoryminer.scm.ISCM;
import org.repositoryminer.util.RMFileUtils;

public class MiningProcessor {

	private static final int COMMITS_RANGE = 3000;

	private ISCM scm;

	private RepositoryMiner repositoryMiner;

	private List<Reference> selectedReferences = new ArrayList<Reference>();
	private Set<String> selectedCommits = new HashSet<String>();
	private Set<PersonIdent> contributors = new HashSet<PersonIdent>();

	private void saveReferences(String repositoryId) {
		ReferenceDAO refDocumentHandler = new ReferenceDAO();
		List<Reference> references = scm.getReferences();

		for (Reference ref : references) {
			ref.setRepository(repositoryId);
			ref.setCommits(scm.getCommitsNames(ref.getPath()));

			Document refDoc = ref.toDocument();
			refDocumentHandler.insert(refDoc);

			saveCommits(repositoryId, ref);

			// copy only the last commit in the reference after saving in the database to
			// save memory
			ref.setCommits(ref.getCommits().subList(0, 1));
			ref.setId(refDoc.getObjectId("_id").toString());
			selectedReferences.add(ref);
		}
	}

	private int saveCommits(String repositoryId, Reference reference) {
		CommitDAO documentHandler = new CommitDAO();

		int skip = 0;
		int acceptedCommits = 0;

		List<Commit> commits = scm.getCommits(skip, COMMITS_RANGE, reference.getPath(), selectedCommits);

		while (commits.size() > 0) {
			List<Document> commitsDoc = new ArrayList<Document>();
			acceptedCommits += commits.size();

			for (Commit commit : commits) {
				commit.setRepository(repositoryId);
				contributors.add(commit.getCommitter());
				commitsDoc.add(commit.toDocument());
				selectedCommits.add(commit.getId());
			}

			documentHandler.insertMany(commitsDoc);
			skip += COMMITS_RANGE;
			commits = scm.getCommits(skip, COMMITS_RANGE, reference.getPath(), selectedCommits);
		}

		return acceptedCommits;
	}

	/**
	 * Starts the mining process
	 * 
	 * @param rm
	 *            instance of {@link org.repositoryminer.mining.RepositoryMiner} .
	 *            It must <b>NEVER<b> be null, since it will provide important
	 *            parameters for the source-code analysis and persistence
	 * @throws IOException
	 */
	public void mine(RepositoryMiner rm) throws IOException {
		this.repositoryMiner = rm;

		File repositoryFolder = new File(rm.getRepositoryPath());
		String tempRepo = RMFileUtils.copyFolderToTmp(repositoryFolder.getAbsolutePath(), rm.getRepositoryName());

		scm = rm.getScm();
		scm.open(tempRepo);

		Repository repository = new Repository(null, rm.getRepositoryKey(), rm.getRepositoryName(),
				rm.getRepositoryPath(), rm.getScm().getSCM(), rm.getRepositoryDescription(),
				new ArrayList<PersonIdent>());

		repository.setPath(FilenameUtils.normalize(repositoryFolder.getAbsolutePath(), true));

		RepositoryDAO repoHandler = new RepositoryDAO();
		Document repoDoc = repository.toDocument();
		repoHandler.insert(repoDoc);
		repository.setId(repoDoc.get("_id").toString());

		saveReferences(repository.getId());
		repoHandler.updateOnlyContributors(repository.getId(), PersonIdent.toDocumentList(contributors));

		calculateAndDetect(tempRepo, repository.getId());

		scm.close();
		RMFileUtils.deleteFolder(tempRepo);
	}

	/**
	 * Performs both the calculation (metrics) and detections (smells/debts) on the
	 * targeted project.
	 * 
	 * @param repositoryMiner
	 *            instance of {@link org.repositoryminer.mining.RepositoryMiner}
	 * @param tempPath
	 *            temporary repository path to access the files content
	 * @throws IOException
	 */
	private void calculateAndDetect(String tempRepo, String repositoryId) throws IOException {
		if (repositoryMiner.getDirectCodeMetrics().size() == 0 && repositoryMiner.getDirectCodeSmells().size() == 0) {
			return;
		}
		
		DirectCodeAnalysisProcessor processor = new DirectCodeAnalysisProcessor();
		processor.setSelectedCommits(new ArrayList<String>(selectedCommits));
		processor.setSCM(scm);
		processor.setRepositoryMiner(repositoryMiner);
		processor.setRepositoryData(repositoryId, tempRepo);
		processor.start();
	}

}