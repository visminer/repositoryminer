package org.repositoryminer.mining;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;
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

/**
 * <h1>The actual mining processor behind
 * {@link org.repositoryminer.mining.RepositoryMiner}</h1>
 * <p>
 * MiningProcessor is a second level entry for the mining API. Direct calls to
 * instances of this class can be made, but we encourage the use of our main
 * facade, {@link org.repositoryminer.mining.RepositoryMiner}, provided it has
 * all the necessary parameters to get the mining process started.
 * <p>
 * It is very important that the injected instance of
 * {@link org.repositoryminer.mining.RepositoryMiner} has all of the mandatory
 * parameters set to valid values so that the persistence of the mined
 * information will be consistent.
 * <p>
 * At least, one list of the following entities must be configured to enable the
 * mining:
 * <ul>
 * <li>Metric -> list of metrics to be calculated. It is assumed that the
 * elements of this list are instances of
 * {@link org.repositoryminer.metric.clazz.IClassMetric}
 * <li>Commits Codesmells -> list of commits-oriented smells to be detected.
 * Each element of this list has to be a sub-type of
 * {@link org.repositoryminer.codesmell.clazz.IClassCodeSmell}
 * <li>Tags Codesmells -> list of tag-related smells to be detected. Each
 * element of this list must be a sub-type of
 * {@link org.repositoryminer.codesmell.project.IProjectCodeSmell}
 * <li>Technical debts -> list of technical debts to be detected. All items of
 * the list must be an instance of any class inherited from
 * {@link org.repositoryminer.technicaldebt.ITechnicalDebt}
 * </ul>
 * At least one of the lists must be populated so to get the mining process
 * started. The lists are then injected in a instance of
 * {@link org.repositoryminer.mining.CommitProcessor} which is capable of
 * performing the actual calculations and detections.
 * <p>
 * Raised exceptions are:
 * <p>
 * <ul>
 * <li>UnsupportedEncodingException, if a unknown text encoding is found in
 * analyzed source-code artifact.
 * </ul>
 * It is up to the caller ignore raised exceptions and skip to next mining step
 * <p>
 */
public class MiningProcessor {

	private ISCM scm;
	private RepositoryMiner repositoryMiner;
	private List<Reference> selectedReferences;

	private void saveReferences(String repositoryId) {
		selectedReferences = new ArrayList<Reference>();
		ReferenceDocumentHandler refDocumentHandler = new ReferenceDocumentHandler();

		for (Reference ref : scm.getReferences()) {
			ref.setRepository(repositoryId);
			ref.setCommits(scm.getReferenceCommits(ref.getPath(), ref.getType()));

			Document refDoc = ref.toDocument();
			refDocumentHandler.insert(refDoc);

			ref.setCommits(null);
			ref.setId(refDoc.getObjectId("_id").toString());
			
			Entry<String, ReferenceType> entry = new AbstractMap.SimpleEntry<String, ReferenceType>(ref.getName(),
					ref.getType());
			if (repositoryMiner.getReferences().contains(entry))
				selectedReferences.add(ref);
		}
	}

	private Set<Contributor> saveCommits(String repositoryId) {
		Set<Contributor> contributors = new HashSet<Contributor>();
		IssueExtractor messageAnalyzer = new IssueExtractor();
		CommitDocumentHandler documentHandler = new CommitDocumentHandler();

		int skip = 0;
		int idx = 0;

		List<Commit> commits = scm.getCommits(skip, repositoryMiner.getCommitCount());

		while (commits != null) {
			List<Document> commitsDoc = new ArrayList<Document>();

			for (Commit commit : commits) {
				commit.setRepository(repositoryId);
				commit.setIssueReferences(messageAnalyzer.analyzeMessage(commit.getMessage()));

				if (repositoryMiner.getMiningListener() != null) {
					repositoryMiner.getMiningListener().commitsProgressChange(++idx, commits.size());
				}

				contributors.add(commit.getCommitter());
				commitsDoc.add(commit.toDocument());
			}

			documentHandler.insertMany(commitsDoc);
			skip += repositoryMiner.getCommitCount();
			commits = scm.getCommits(skip, repositoryMiner.getCommitCount());
		}

		return contributors;
	}

	/**
	 * Starts the mining process
	 * 
	 * @param repositoryMiner
	 *            instance of {@link org.repositoryminer.mining.RepositoryMiner}
	 *            . It must <b>NEVER<b> be null, since it will provide important
	 *            parameters for the source-code analysis and persistence
	 * @throws IOException
	 */
	public Repository mine(RepositoryMiner repositoryMiner) throws IOException {
		RepositoryDocumentHandler repoHandler = new RepositoryDocumentHandler();
		List<Document> repos = repoHandler.findRepositoriesByName(repositoryMiner.getName());
		Repository repository = Repository.parseDocument(repos.get(0));

		if (repository != null) {
			return repository;
		}

		File repositoryFolder = new File(repositoryMiner.getPath());
		String tempRepo = FileUtils.copyFolderToTmp(repositoryFolder.getAbsolutePath(), repositoryMiner.getName());

		scm = SCMFactory.getSCM(repositoryMiner.getScm());
		scm.open(tempRepo);

		repository = new Repository(repositoryMiner);
		repository.setPath(FilenameUtils.normalize(repositoryFolder.getAbsolutePath(), true));

		Document repoDoc = repository.toDocument();
		repoHandler.insert(repoDoc);
		repository.setId(repoDoc.get("_id").toString());

		saveReferences(repository.getId());
		Set<Contributor> contributors = saveCommits(repository.getId());

		repoDoc.append("contributors", Contributor.toDocumentList(contributors));
		repoHandler.updateOnlyContributors(repoDoc);
		repository.setContributors(new ArrayList<Contributor>(contributors));

		scm.close();
		FileUtils.deleteFolder(tempRepo);

		return repository;
	}

}