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

public class MiningProcessor {

	public MiningProcessor() {
	}

	private void calculateAndDetect(RepositoryMiner repositoryMiner, List<CommitDB> commits, List<ReferenceDB> scmRefs, List<ReferenceDB> timeRefs,
			SCM scm, String repoId, String repoPath) throws UnsupportedEncodingException {

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