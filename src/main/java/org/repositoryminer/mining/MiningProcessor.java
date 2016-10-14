package org.repositoryminer.mining;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Contributor;
import org.repositoryminer.model.Reference;
import org.repositoryminer.model.Repository;
import org.repositoryminer.model.WorkingDirectory;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.handler.RepositoryDocumentHandler;
import org.repositoryminer.persistence.handler.WorkingDirectoryDocumentHandler;
import org.repositoryminer.scm.ISCM;
import org.repositoryminer.scm.SCMFactory;
import org.repositoryminer.utility.StringUtils;

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
 * {@link org.repositoryminer.mining.SourceAnalyzer} which is capable of
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
	private List<Reference> references;
	private List<Reference> timeReferences;
	private List<Commit> commits;
	private Repository repository;
	private Set<Contributor> contributors;

	String copyRepositoryFolder(String srcFolder, String destFolderName) throws IOException {
		File src = new File(srcFolder);
		File dest = new File(System.getProperty("java.io.tmpdir"), destFolderName);
		FileUtils.copyDirectory(src, dest);
		return StringUtils.normalizePath(dest.getCanonicalPath());
	}

	private void deleteRepositoryFolder(String folderName) throws IOException {
		File folder = new File(folderName);
		FileUtils.deleteDirectory(folder);
	}

	private void saveReferences(String repositoryId) {
		ReferenceDocumentHandler refHandler = new ReferenceDocumentHandler();
		references = scm.getReferences();
		for (Reference ref : references) {
			ref.setRepository(repositoryId);
			ref.setCommits(scm.getReferenceCommits(ref.getPath(), ref.getType()));
			refHandler.insert(ref.toDocument());
		}
	}

	/*
	 * This method is used to create a hash map using the commit hash as key and
	 * the commit object as value. The main objective of the method is to make
	 * faster the time frame processing. This processing will require a lot of
	 * searches in some structure, and a hash map is perfect for this job,
	 * because its average time complexity is O(1). It is preferable use this
	 * process instead of bring back the commits in the reference, because get
	 * the commits with the changes made and other data require more resources
	 * and requires disk I/O. And these data is already in the class, so making
	 * N searches in O(1) time complexity will costs O(N).
	 */
	private Map<String, Commit> createCommitsMap() {
		Map<String, Commit> commitsMap = new HashMap<String, Commit>(commits.size());
		for (Commit c : commits) {
			commitsMap.put(c.getId(), c);
		}
		return commitsMap;
	}

	private void saveTimeReferences(RepositoryMiner rm) {
		if (rm.getReferences() == null) {
			return;
		}

		boolean proceed = false;
		for (Entry<Reference, TimeFrameType[]> entry : rm.getReferences().entrySet()) {
			if (entry.getValue() != null) {
				proceed = true;
				break;
			}
		}

		if (!proceed) {
			return;
		}

		Map<String, Commit> commitsMap = createCommitsMap();
		ProcessTimeFrames procTimeFrames = new ProcessTimeFrames(repository.getId(), commitsMap,
				rm.getMiningListener());
		timeReferences = new ArrayList<Reference>();

		for (Entry<Reference, TimeFrameType[]> entry : rm.getReferences().entrySet()) {
			if (entry.getValue() != null) {
				int index = references.indexOf(entry.getKey());
				timeReferences.addAll(procTimeFrames.analyzeCommits(references.get(index), entry.getValue()));
			}
		}

	}

	private void saveCommitsAndSnapshots(RepositoryMiner repositoryMiner) {
		contributors = new HashSet<Contributor>();
		CommitDocumentHandler commitHandler = new CommitDocumentHandler();

		WorkingDirectory wd = new WorkingDirectory(repository.getId());
		WorkingDirectoryDocumentHandler wdHandler = new WorkingDirectoryDocumentHandler();

		CommitMessageAnalyzer messageAnalyzer = new CommitMessageAnalyzer();

		commits = scm.getCommits();
		int idx = 0;

		for (Commit commit : commits) {
			commit.setRepository(repository.getId());
			commit.setIssueReferences(messageAnalyzer.analyzeMessage(commit.getMessage()));

			if (repositoryMiner.getMiningListener() != null) {
				repositoryMiner.getMiningListener().commitsProgressChange(++idx, commits.size());
			}

			contributors.add(commit.getCommitter());
			wd.setId(commit.getId());
			wd.processDiff(commit.getDiffs());
			wdHandler.insert(wd.toDocument());
			commitHandler.insert(commit.toDocument());
		}
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
		
		if (repos != null && !repos.isEmpty()) {
			repository = Repository.parseDocument(repos.get(0));
		} else {
			String tempRepo = copyRepositoryFolder(repositoryMiner.getPath(), repositoryMiner.getName());

			scm = SCMFactory.getSCM(repositoryMiner.getScm());
			scm.open(tempRepo);

			repository = new Repository(repositoryMiner);

			Document repoDoc = repository.toDocument();
			repoHandler.insert(repoDoc);
			repository.setId(repoDoc.get("_id").toString());

			saveReferences(repository.getId());
			saveCommitsAndSnapshots(repositoryMiner);
			saveTimeReferences(repositoryMiner);

			calculateAndDetect(repositoryMiner, tempRepo);

			repoDoc.append("contributors", Contributor.toDocumentList(contributors));
			repoHandler.updateOnlyContributors(repoDoc);

			scm.close();
			deleteRepositoryFolder(tempRepo);
		}

		return repository;
	}

	/**
	 * Performs both the calculation (metrics) and detections (smells/debts) on
	 * the artifacts of a targeted project. An instance of
	 * {@link org.repositoryminer.mining.SourceAnalyzer} is prepared/configured
	 * and put into action (
	 * {@link org.repositoryminer.mining.SourceAnalyzer#analyze()})
	 * 
	 * @param repositoryMiner
	 *            instance of {@link org.repositoryminer.mining.RepositoryMiner}
	 * @param tempPath
	 *            temporary repository path to access the files content
	 * @throws IOException
	 */
	private void calculateAndDetect(RepositoryMiner repositoryMiner, String tempPath) throws IOException {

		if (!repositoryMiner.hasClassCodeSmells() && !repositoryMiner.hasClassMetrics()
				&& !repositoryMiner.hasTechnicalDebts() && !repositoryMiner.hasProjectsCodeSmells()) {
			return;
		}

		List<Reference> refs = null;
		if (repositoryMiner.hasProjectsCodeSmells()) {
			refs = new ArrayList<Reference>();

			for (Reference ref : repositoryMiner.getReferences().keySet()) {
				int index = references.indexOf(ref);
				refs.add(references.get(index));
			}

			if (timeReferences != null) {
				for (Reference ref : timeReferences) {
					refs.add(ref);
				}
			}
		}

		Map<String, Commit> commitsMap = createCommitsMap();
		SourceAnalyzer sourceAnalyzer = new SourceAnalyzer(repositoryMiner, scm, repository.getId(), tempPath);

		sourceAnalyzer.setCommitsMap(commitsMap);
		sourceAnalyzer.setReferences(refs);
		sourceAnalyzer.analyze();
	}

}