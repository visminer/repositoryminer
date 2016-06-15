package org.repositoryminer.mining;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.handler.RepositoryDocumentHandler;
import org.repositoryminer.persistence.handler.WorkingDirectoryDocumentHandler;
import org.repositoryminer.persistence.model.Commit;
import org.repositoryminer.persistence.model.Contributor;
import org.repositoryminer.persistence.model.Reference;
import org.repositoryminer.persistence.model.Repository;
import org.repositoryminer.persistence.model.WorkingDirectory;
import org.repositoryminer.scm.SCM;
import org.repositoryminer.scm.SCMFactory;
import org.repositoryminer.utility.HashHandler;

public class RepositoryMiner {

	private List<SCMRepository> repositories = null;

	public RepositoryMiner setRepositories(SCMRepository... repositories) {
		this.repositories = Arrays.asList(repositories);
		return this;
	}

	public RepositoryMiner setRepositories(List<SCMRepository> repositories) {
		this.repositories = repositories;
		return this;
	}

	public List<SCMRepository> getRepositories() {
		return repositories;
	}

	public void mine() throws UnsupportedEncodingException {
		for (SCMRepository repository : repositories) {
			SCM scm = SCMFactory.getSCM(repository.getScm());
			scm.open(repository.getPath(), repository.getBinaryThreshold());

			String absPath = scm.getAbsolutePath();
			String id = HashHandler.SHA1(absPath);

			Repository r = new Repository(repository);
			r.setId(id);
			r.setPath(absPath);
			
			WorkingDirectory wd = new WorkingDirectory(id);
			WorkingDirectoryDocumentHandler wdHandler = new WorkingDirectoryDocumentHandler();
			
			ReferenceDocumentHandler refHandler = new ReferenceDocumentHandler();
			List<Reference> refs = scm.getReferences();
			for (Reference ref : refs) {
				ref.setCommits(scm.getReferenceCommits(ref.getFullName(), ref.getType()));
				refHandler.insert(ref.toDocument());
				ref.setCommits(null);
			}

			int skip = 0;
			Set<Contributor> contributors = new HashSet<Contributor>();
			CommitDocumentHandler commitHandler = new CommitDocumentHandler();

			SourceAnalyzer sourceAnalyzer = null;
			if (repository.getMetrics() != null || repository.getCodeSmells() != null
					|| repository.getTechnicalDebts() != null) {
				sourceAnalyzer = new SourceAnalyzer(repository, scm, id, absPath);
			}

			while (true) {
				List<Commit> commits = scm.getCommits(skip, repository.getCommitThreshold());
				List<Document> docs = new ArrayList<Document>();
				if (commits.size() == 0)
					break;

				for (Commit c : commits) {
					docs.add(c.toDocument());
					contributors.add(c.getCommitter());
					wd.setId(c.getId());
					wd.processDiff(c.getDiffs());
					wdHandler.insert(wd.toDocument());
				}

				commitHandler.insertMany(docs);
				if (sourceAnalyzer != null)
					sourceAnalyzer.analyze(commits);

				if (repository.getCommitThreshold() == 0)
					break;
				skip += repository.getCommitThreshold();
			}

			RepositoryDocumentHandler repoHandler = new RepositoryDocumentHandler();
			r.setContributors(new ArrayList<Contributor>(contributors));
			repoHandler.insert(r.toDocument());

			scm.close();
		}
	}

}