package org.repositoryminer.mining.local;

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
 * {@link org.repositoryminer.codemetric.direct.IDirectCodeMetric}
 * <li>Commits Codesmells -> list of commits-oriented smells to be detected.
 * Each element of this list has to be a sub-type of
 * {@link org.repositoryminer.codesmell.direct.IDirectCodeSmell}
 * <li>Tags Codesmells -> list of tag-related smells to be detected. Each
 * element of this list must be a sub-type of
 * {@link org.repositoryminer.codesmell.project.IProjectCodeSmell}
 * <li>Technical debts -> list of technical debts to be detected. All items of
 * the list must be an instance of any class inherited from
 * {@link org.repositoryminer.technicaldebt.ITechnicalDebt}
 * </ul>
 * At least one of the lists must be populated so to get the mining process
 * started. The lists are then injected in a instance of
 * {@link org.repositoryminer.mining.local.DirectCodeAnalysisProcessor} which is
 * capable of performing the actual calculations and detections.
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

	private static final int COMMITS_RANGE = 3000;

	private ISCM scm;
	private IssueExtractor messageAnalyzer;
	
	private RepositoryMiner repositoryMiner;
	private IMiningListener listener;
	
	private List<Reference> selectedReferences;
	private Set<String> selectedCommits;
	private Set<Contributor> contributors;

	private void saveReferences(String repositoryId) {
		selectedReferences = new ArrayList<Reference>();
		contributors = new HashSet<Contributor>();
		selectedCommits = new HashSet<String>();
		messageAnalyzer = new IssueExtractor();

		ReferenceDocumentHandler refDocumentHandler = new ReferenceDocumentHandler();
		List<Reference> references = scm.getReferences();
		
		listener.notifyReferencesMiningStart(references.size());
		
		for (Reference ref : references) {
			Entry<String, ReferenceType> entry = new AbstractMap.SimpleEntry<String, ReferenceType>(ref.getName(),
					ref.getType());

			if (!repositoryMiner.getReferences().contains(entry)) {
				continue;
			}

			listener.notifyReferencesMiningProgress(ref.getName(), ref.getType());
			
			ref.setRepository(repositoryId);
			ref.setCommits(scm.getReferenceCommits(ref.getPath(), ref.getType()));

			Document refDoc = ref.toDocument();
			refDocumentHandler.insert(refDoc);

			listener.notifyCommitsMiningStart(ref.getName(), ref.getType(), ref.getCommits().size());
			listener.notifyCommitsMiningEnd(ref.getName(), ref.getType(), saveCommits(repositoryId, ref));
			
			ref.setCommits(ref.getCommits().subList(0, 1)); // copy only the last commit in the reference
			ref.setId(refDoc.getObjectId("_id").toString());
			selectedReferences.add(ref);
		}
		
		listener.notifyReferencesMiningEnd(selectedReferences.size());
	}

	private int saveCommits(String repositoryId, Reference reference) {
		CommitDocumentHandler documentHandler = new CommitDocumentHandler();

		int skip = 0;
		int acceptedCommits = 0;
		
		List<Commit> commits = scm.getCommits(skip, COMMITS_RANGE, reference, selectedCommits);

		while (commits.size() > 0) {
			List<Document> commitsDoc = new ArrayList<Document>();
			acceptedCommits += commits.size();
			
			for (Commit commit : commits) {
				listener.notifyCommitsMiningProgress(reference.getName(), reference.getType(), commit.getId());
				
				commit.setRepository(repositoryId);
				commit.setIssueReferences(messageAnalyzer.analyzeMessage(commit.getMessage()));

				contributors.add(commit.getCommitter());
				commitsDoc.add(commit.toDocument());

				selectedCommits.add(commit.getId());
			}

			documentHandler.insertMany(commitsDoc);
			skip += COMMITS_RANGE;
			commits = scm.getCommits(skip, COMMITS_RANGE, reference, selectedCommits);
		}
		
		return acceptedCommits;
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
	public void mine(RepositoryMiner repositoryMiner) throws IOException {
		this.repositoryMiner = repositoryMiner;
		this.listener = repositoryMiner.getMiningListener();

		listener.notifyMiningStart(repositoryMiner.getName());
		
		File repositoryFolder = new File(repositoryMiner.getPath());
		String tempRepo = FileUtils.copyFolderToTmp(repositoryFolder.getAbsolutePath(), repositoryMiner.getName());

		scm = SCMFactory.getSCM(repositoryMiner.getScm());
		scm.open(tempRepo);

		Repository repository = new Repository(repositoryMiner);
		repository.setPath(FilenameUtils.normalize(repositoryFolder.getAbsolutePath(), true));

		RepositoryDocumentHandler repoHandler = new RepositoryDocumentHandler();
		Document repoDoc = repository.toDocument();
		repoHandler.insert(repoDoc);
		repository.setId(repoDoc.get("_id").toString());

		saveReferences(repository.getId());
		repoHandler.updateOnlyContributors(repository.getId(), Contributor.toDocumentList(contributors));

		saveWorkingDirectories(repository.getId());
		calculateAndDetect(tempRepo, repository.getId());

		scm.close();
		FileUtils.deleteFolder(tempRepo);
		
		listener.notifyMiningEnd(repositoryMiner.getName());
	}

	private void saveWorkingDirectories(String repositoryId) {
		WorkingDirectoryProcessor wdProcessor = new WorkingDirectoryProcessor();
		wdProcessor.setReferences(selectedReferences);
		wdProcessor.setRepositoryId(repositoryId);
		wdProcessor.processWorkingDirectories();
		wdProcessor.setListener(listener);
	}

	/**
	 * Performs both the calculation (metrics) and detections (smells/debts) on
	 * the targeted project.
	 * 
	 * @param repositoryMiner
	 *            instance of {@link org.repositoryminer.mining.RepositoryMiner}
	 * @param tempPath
	 *            temporary repository path to access the files content
	 * @throws IOException
	 */
	private void calculateAndDetect(String tempRepo, String repositoryId) throws IOException {
		if (!repositoryMiner.hasParsers() || selectedReferences.size() == 0) {
			return;
		}

		if (repositoryMiner.hasDirectCodeMetrics() || repositoryMiner.hasDirectCodeSmells()) {
			DirectCodeAnalysisProcessor processor = new DirectCodeAnalysisProcessor();
			processor.setSelectedCommits(new ArrayList<String>(selectedCommits));
			processor.setSCM(scm);
			processor.setRepositoryMiner(repositoryMiner);
			processor.setRepositoryData(repositoryId, tempRepo);
			processor.start();
		}

		if (repositoryMiner.hasIndirectCodeMetrics() || repositoryMiner.hasIndirectCodeSmells()) {
			List<String> validSnapshots = new ArrayList<String>();
			for (String hash : repositoryMiner.getSnapshots()) {
				if (selectedCommits.contains(hash)) {
					validSnapshots.add(hash);
				}
			}

			IndirectCodeAnalysisProcessor processor = new IndirectCodeAnalysisProcessor();
			processor.setReferences(selectedReferences);
			processor.setSnapshots(validSnapshots);
			processor.setRepositoryData(repositoryId, tempRepo);
			processor.setRepositoryMiner(repositoryMiner);
			processor.setSCM(scm);
			processor.start();
		}
	}

}