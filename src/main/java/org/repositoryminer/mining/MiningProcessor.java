package org.repositoryminer.mining;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.repositoryminer.listener.IProgressListener;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.handler.RepositoryDocumentHandler;
import org.repositoryminer.persistence.handler.WorkingDirectoryDocumentHandler;
import org.repositoryminer.persistence.model.CommitDB;
import org.repositoryminer.persistence.model.ContributorDB;
import org.repositoryminer.persistence.model.ReferenceDB;
import org.repositoryminer.persistence.model.RepositoryDB;
import org.repositoryminer.persistence.model.WorkingDirectoryDB;
import org.repositoryminer.scm.SCM;
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
 * {@link org.repositoryminer.metric.ICommitMetric}
 * <li>Commits Codesmells -> list of commits-oriented smells to be detected.
 * Each element of this list has to be a sub-type of
 * {@link org.repositoryminer.codesmell.commit.ICommitCodeSmell}
 * <li>Tags Codesmells -> list of tag-related smells to be detected. Each
 * element of this list must be a sub-type of
 * {@link org.repositoryminer.codesmell.tag.ITagCodeSmell}
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

	public MiningProcessor() {
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
	 * @param commits
	 *            list of all commits from project's repository
	 * @param scmRefs
	 * @param timeRefs
	 * @param scm
	 * @param repoId
	 *            hash to uniquely identify the repository
	 * @param repoPath
	 *            path to the project being mined
	 * @throws UnsupportedEncodingException
	 */
	private void calculateAndDetect(RepositoryMiner repositoryMiner, List<CommitDB> commits, List<ReferenceDB> scmRefs,
			List<ReferenceDB> timeRefs, SCM scm, String repoId, String repoPath) throws UnsupportedEncodingException {

		boolean commitMetrics = (repositoryMiner.getCommitMetrics() != null
				&& repositoryMiner.getCommitMetrics().size() > 0);
		boolean commitTechnicalDebts = (repositoryMiner.getTechnicalDebts() != null
				&& repositoryMiner.getTechnicalDebts().size() > 0);
		boolean commitCodeSmells = (repositoryMiner.getCommitCodeSmells() != null
				&& repositoryMiner.getCommitCodeSmells().size() > 0);
		boolean tagCodeSmells = (repositoryMiner.getTagCodeSmells() != null
				&& repositoryMiner.getTagCodeSmells().size() > 0);

		if (!commitCodeSmells && !commitMetrics && !commitTechnicalDebts && !tagCodeSmells) {
			return;
		}

		List<ReferenceDB> tags = null;
		if (tagCodeSmells) {
			tags = new ArrayList<ReferenceDB>();
			for (ReferenceDB ref : scmRefs) {
				tags.add(ref);
			}
			for (ReferenceDB ref : timeRefs) {
				tags.add(ref);
			}
		}

		SourceAnalyzer sourceAnalyzer = new SourceAnalyzer(repositoryMiner, scm, repoId, repoPath);
		sourceAnalyzer.setCommitCodeSmells(commitCodeSmells);
		sourceAnalyzer.setCommitMetrics(commitMetrics);
		sourceAnalyzer.setCommits(commits);
		sourceAnalyzer.setCommitTechnicalDebts(commitTechnicalDebts);
		sourceAnalyzer.setTagCodeSmells(tagCodeSmells);
		sourceAnalyzer.setTags(tags);

		sourceAnalyzer.analyze();
	}

	/**
	 * Starts the mining process
	 * 
	 * @param repositoryMiner
	 *            instance of {@link org.repositoryminer.mining.RepositoryMiner}
	 *            . It must <b>NEVER<b> be null, since it will provide important
	 *            parameters for the source-code analysis and persistence
	 * @throws UnsupportedEncodingException
	 */
	public void mine(RepositoryMiner repositoryMiner) throws UnsupportedEncodingException {
		SCM scm = SCMFactory.getSCM(repositoryMiner.getScm());
		scm.open(repositoryMiner.getPath(), repositoryMiner.getBinaryThreshold());

		String absPath = scm.getAbsolutePath();
		String id = StringUtils.encodeToSHA1(absPath);

		RepositoryDB r = new RepositoryDB(repositoryMiner);
		r.setId(id);
		r.setPath(absPath);

		WorkingDirectoryDB wd = new WorkingDirectoryDB(id);
		WorkingDirectoryDocumentHandler wdHandler = new WorkingDirectoryDocumentHandler();

		ReferenceDocumentHandler refHandler = new ReferenceDocumentHandler();
		List<ReferenceDB> refs = scm.getReferences();
		for (ReferenceDB ref : refs) {
			ref.setCommits(scm.getReferenceCommits(ref.getFullName(), ref.getType()));
			refHandler.insert(ref.toDocument());
		}

		Set<ContributorDB> contributors = new HashSet<ContributorDB>();
		CommitDocumentHandler commitHandler = new CommitDocumentHandler();

		List<CommitDB> commits = scm.getCommits();
		List<Document> docs = new ArrayList<Document>();

		IProgressListener progressListener = repositoryMiner.getProgressListener();

		int idx = 0;
		for (CommitDB c : commits) {
			if (progressListener != null) {
				progressListener.commitsProgressChange(++idx, commits.size());
			}

			docs.add(c.toDocument());
			contributors.add(c.getCommitter());
			wd.setId(c.getId());
			wd.processDiff(c.getDiffs());
			wdHandler.insert(wd.toDocument());
		}

		commitHandler.insertMany(docs);

		List<ReferenceDB> timeRefs = new ArrayList<ReferenceDB>();
		if (repositoryMiner.getTimeFrames() != null) {
			if (progressListener != null) {
				progressListener.initTimeFramesProcessingProgress();
			}

			ProcessTimeFrames procTimeFrames = new ProcessTimeFrames(absPath, id);
			timeRefs = procTimeFrames.analyzeCommits(commits, repositoryMiner.getTimeFrames(), progressListener);
		}

		calculateAndDetect(repositoryMiner, commits, refs, timeRefs, scm, id, absPath);

		RepositoryDocumentHandler repoHandler = new RepositoryDocumentHandler();
		r.setContributors(new ArrayList<ContributorDB>(contributors));
		repoHandler.insert(r.toDocument());

		scm.close();
	}

}