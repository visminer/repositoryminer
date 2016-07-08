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

public class RepositoryMiner {

	public static void mine(SCMRepository repository) throws UnsupportedEncodingException {
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
			ref.setCommits(null);
		}

		Set<ContributorDB> contributors = new HashSet<ContributorDB>();
		CommitDocumentHandler commitHandler = new CommitDocumentHandler();

		SourceAnalyzer sourceAnalyzer = null;
		if (repository.getMetrics() != null || repository.getCodeSmells() != null
				|| repository.getTechnicalDebts() != null) {
			sourceAnalyzer = new SourceAnalyzer(repository, scm, id, absPath);
		}

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
		if (sourceAnalyzer != null) {
			sourceAnalyzer.analyze(commits);
		}

		RepositoryDocumentHandler repoHandler = new RepositoryDocumentHandler();
		r.setContributors(new ArrayList<ContributorDB>(contributors));
		repoHandler.insert(r.toDocument());

		scm.close();
	}

}