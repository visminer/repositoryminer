package org.repositoryminer.mining;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.repositoryminer.listener.IMiningListener;
import org.repositoryminer.listener.IProgressListener;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.model.CommitDB;
import org.repositoryminer.persistence.model.ContributorDB;
import org.repositoryminer.persistence.model.ReferenceDB;
import org.repositoryminer.persistence.model.RepositoryDB;
import org.repositoryminer.persistence.model.WorkingDirectoryDB;
import org.repositoryminer.scm.SCM;
import org.repositoryminer.scm.SCMFactory;
import org.repositoryminer.utility.HashHandler;

public class MiningProcessor {

	private RepositoryMiner repositoryMiner;
	
	private List<CommitDB> commits;
	private List<ReferenceDB> scmRefs;
	private List<ReferenceDB> timeRefs;
	
	private IProgressListener progressListener;
	private IMiningListener miningListener;
	
	private SCM scm;
	private String repoId, repoPath;

	public MiningProcessor(RepositoryMiner repositoryMiner) {
		this.repositoryMiner = repositoryMiner;
	}
	
	public MiningProcessor setProgressListener(IProgressListener listener) {
		progressListener = listener;
		
		return this;
	}
	
	public MiningProcessor setMiningListener(IMiningListener listener) {
		miningListener = listener;
		
		return this;
	}
	
	private void calculateAndDetect()
					throws UnsupportedEncodingException {
		boolean commitMetrics = (repositoryMiner.getCommitMetrics() != null
				&& repositoryMiner.getCommitMetrics().size() > 0);
		boolean commitTechnicalDebts = (repositoryMiner.getTechnicalDebts() != null
				&& repositoryMiner.getTechnicalDebts().size() > 0);
		boolean commitCodeSmells = (repositoryMiner.getCommitCodeSmells() != null
				&& repositoryMiner.getCommitCodeSmells().size() > 0);
		boolean tagCodeSmells = (repositoryMiner.getTagCodeSmells() != null
				&& repositoryMiner.getTagCodeSmells().size() > 0);

		if (!commitCodeSmells && !commitMetrics && !commitTechnicalDebts && !tagCodeSmells) {
			//FIXME raise exception when no processing is required
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

		SourceAnalyzer sourceAnalyzer = new SourceAnalyzer(repositoryMiner, miningListener, progressListener, scm,
				repoId, repoPath);
		sourceAnalyzer.setCommitCodeSmells(commitCodeSmells);
		sourceAnalyzer.setCommitMetrics(commitMetrics);
		sourceAnalyzer.setCommits(commits);
		sourceAnalyzer.setCommitTechnicalDebts(commitTechnicalDebts);
		sourceAnalyzer.setTagCodeSmells(tagCodeSmells);
		sourceAnalyzer.setTags(tags);

		sourceAnalyzer.analyze();
	}

	public void mine()
					throws UnsupportedEncodingException {
		scm = SCMFactory.getSCM(repositoryMiner.getScm());
		scm.open(repositoryMiner.getPath(), repositoryMiner.getBinaryThreshold());

		repoPath = scm.getAbsolutePath();
		repoId = HashHandler.SHA1(repoPath);

		RepositoryDB r = new RepositoryDB(repositoryMiner);
		r.setId(repoId);
		r.setPath(repoPath);

		WorkingDirectoryDB wd = new WorkingDirectoryDB(repoId);

		ReferenceDocumentHandler refHandler = new ReferenceDocumentHandler();
		List<ReferenceDB> refs = scm.getReferences();
		for (ReferenceDB ref : refs) {
			ref.setCommits(scm.getReferenceCommits(ref.getFullName(), ref.getType()));
			refHandler.insert(ref.toDocument());
		}

		Set<ContributorDB> contributors = new HashSet<ContributorDB>();
		commits = scm.getCommits();
		progressListener.initCommitsProcessingProgress(commits.size());
		int idx = 0;
		for (CommitDB c : commits) {
			progressListener.commitProgressChange(++idx, commits.size());

			contributors.add(c.getCommitter());
			wd.setId(c.getId());
			wd.processDiff(c.getDiffs());

			miningListener.updateCommit(c);
			miningListener.updateWorkingDirectory(wd);
		}

		timeRefs = new ArrayList<ReferenceDB>();
		if (repositoryMiner.getTimeFrames() != null) {
			progressListener.initTimeFramesProcessingProgress();

			ProcessTimeFrames procTimeFrames = new ProcessTimeFrames(repoPath, repoId, progressListener);
			timeRefs = procTimeFrames.analyzeCommits(commits, repositoryMiner.getTimeFrames());
		}

		calculateAndDetect();

		miningListener.updateRepository(r);

		scm.close();

		progressListener.endOfProcessingProgress();
	}

}