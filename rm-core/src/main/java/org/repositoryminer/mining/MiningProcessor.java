package org.repositoryminer.mining;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import org.repositoryminer.domain.Commit;
import org.repositoryminer.domain.PersonIdent;
import org.repositoryminer.domain.Reference;
import org.repositoryminer.domain.Repository;
import org.repositoryminer.exception.RepositoryMinerException;
import org.repositoryminer.persistence.dao.CommitDAO;
import org.repositoryminer.persistence.dao.ReferenceDAO;
import org.repositoryminer.persistence.dao.RepositoryDAO;
import org.repositoryminer.scm.ISCM;
import org.repositoryminer.util.RMFileUtils;

public class MiningProcessor {

	private ISCM scm;
	private RepositoryMiner rm;
	private Set<String> selectedCommits = new LinkedHashSet<String>();

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
		File repositoryFolder = new File(rm.getRepositoryPath());
		String tempRepo = RMFileUtils.copyFolderToTmp(repositoryFolder.getAbsolutePath(), rm.getRepositoryName());

		this.rm = rm;
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
		repoHandler.updateOnlyContributors(repository.getId(),
				PersonIdent.toDocumentList(saveCommits(repository.getId())));

		startCodeAnalysis(repository.getId(), tempRepo);

		scm.close();
		RMFileUtils.deleteFolder(tempRepo);
	}

	private void startCodeAnalysis(String repoId, String repoPath) {
		if (!rm.hasParsers() || selectedCommits.size() == 0)
			return;
		
		CodeAnalysisProcessor codeAnalysis = new CodeAnalysisProcessor();
		codeAnalysis.setRepoId(repoId);
		codeAnalysis.setRepoPath(repoPath);
		codeAnalysis.setRm(rm);
		codeAnalysis.setSelectedCommits(selectedCommits);
		
		try {
			codeAnalysis.start();
		} catch (IOException e) {
			throw new RepositoryMinerException(e.getMessage());
		}
	}

	private void saveReferences(String repositoryId) {
		ReferenceDAO refDocumentHandler = new ReferenceDAO();
		List<Reference> references = scm.getReferences();

		for (Reference ref : references) {
			List<String> commits = scm.getCommitsNames(ref);

			ref.setRepository(repositoryId);
			ref.setCommits(commits);

			Document refDoc = ref.toDocument();
			refDocumentHandler.insert(refDoc);

			if (rm.hasReferences() && rm.getReferences().contains(new ReferenceEntry(ref.getName(), ref.getType()))) {
				Collections.reverse(commits);
				selectedCommits.addAll(commits);
			}
		}
	}

	private Set<PersonIdent> saveCommits(String repositoryId) {
		CommitDAO documentHandler = new CommitDAO();
		Set<PersonIdent> contributors = new HashSet<PersonIdent>();

		for (Commit commit : scm.getCommits()) {
			commit.setRepository(repositoryId);
			contributors.add(commit.getCommitter());
			documentHandler.insert(commit.toDocument());
		}

		return contributors;
	}

}