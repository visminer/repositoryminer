package org.repositoryminer.mining;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
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
import org.repositoryminer.utility.HashHandler;

public class MiningProcessor {

	private static void calculateMeasures(RepositoryMiner repoMiner, List<CommitDB> commits, List<ReferenceDB> scmRefs,
			List<ReferenceDB> timeRefs, SCM scm, String repoId, String repoPath) throws UnsupportedEncodingException {
		
		boolean commitMetrics = (repoMiner.getCommitMetrics() != null && repoMiner.getCommitMetrics().size() > 0) ? true : false;
		boolean commitTechnicalDebts = (repoMiner.getTechnicalDebts() != null
				&& repoMiner.getTechnicalDebts().size() > 0) ? true : false;
		boolean commitCodeSmells = (repoMiner.getCommitCodeSmells() != null
				&& repoMiner.getCommitCodeSmells().size() > 0) ? true : false;
		boolean tagCodeSmells = (repoMiner.getTagCodeSmells() != null && repoMiner.getTagCodeSmells().size() > 0) ? true
				: false;

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

		SourceAnalyzer sourceAnalyzer = new SourceAnalyzer(repoMiner, scm, repoId, repoPath);
		sourceAnalyzer.setCommitCodeSmells(commitCodeSmells);
		sourceAnalyzer.setCommitMetrics(commitMetrics);
		sourceAnalyzer.setCommits(commits);
		sourceAnalyzer.setCommitTechnicalDebts(commitTechnicalDebts);
		sourceAnalyzer.setTagCodeSmells(tagCodeSmells);
		sourceAnalyzer.setTags(tags);
		
		sourceAnalyzer.analyze();
	}

	public static void mine(RepositoryMiner repository) throws UnsupportedEncodingException {
		SCM scm = SCMFactory.getSCM(repository.getScm());
		scm.open(repository.getPath(), repository.getBinaryThreshold());

		String absPath = scm.getAbsolutePath();
		String id = HashHandler.SHA1(absPath);

		RepositoryDB r = new RepositoryDB(repository);
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

		for (CommitDB c : commits) {
			docs.add(c.toDocument());
			contributors.add(c.getCommitter());
			wd.setId(c.getId());
			wd.processDiff(c.getDiffs());
			wdHandler.insert(wd.toDocument());
		}

		commitHandler.insertMany(docs);
		
		List<ReferenceDB> timeRefs = new ArrayList<ReferenceDB>();
		if (repository.getTimeFrames() != null) {
			ProcessTimeFrames procTimeFrames = new ProcessTimeFrames(absPath, id);
			timeRefs = procTimeFrames.analyzeCommits(commits, repository.getTimeFrames());
		}

		calculateMeasures(repository, commits, refs, timeRefs, scm, id, absPath);
		
		RepositoryDocumentHandler repoHandler = new RepositoryDocumentHandler();
		r.setContributors(new ArrayList<ContributorDB>(contributors));
		repoHandler.insert(r.toDocument());

		scm.close();
	}

}