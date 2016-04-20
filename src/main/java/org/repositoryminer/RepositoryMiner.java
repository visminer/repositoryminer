package org.repositoryminer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Contributor;
import org.repositoryminer.model.Reference;
import org.repositoryminer.model.Repository;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.handler.RepositoryDocumentHandler;
import org.repositoryminer.scm.SCM;
import org.repositoryminer.scm.SCMFactory;
import org.repositoryminer.scm.SCMRepository;
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
	
	public List<SCMRepository> getRepositories(){
		return repositories;
	}
	
	public void mine() {
		for(SCMRepository repository : repositories){
			SCM scm = SCMFactory.getSCM(repository.getScm());
			scm.open(repository.getPath());
			
			String absPath = scm.getAbsolutePath();
			String id = HashHandler.SHA1(absPath);
			
			Repository r = new Repository(repository);
			r.setId(id);
			r.setPath(absPath);
			
			ReferenceDocumentHandler refHandler = new ReferenceDocumentHandler();
			List<Reference> refs = scm.getReferences();
			for(Reference ref : refs){
				ref.setCommits(scm.getReferenceCommits(ref.getFullName(), ref.getType()));
				refHandler.inser(ref.toDocument());
				ref.setCommits(null);
			}
			
			int skip = 0;
			Set<Contributor> contributors = new HashSet<Contributor>();
			CommitDocumentHandler commitHandler = new CommitDocumentHandler();
			while(true) {
				List<Commit> commits = scm.getCommits(skip, repository.getCommitThreshold());
				if(commits.size() == 0) break;
				skip += repository.getCommitThreshold();
				
				List<Document> docs = new ArrayList<Document>();
				for(Commit c : commits){
					docs.add(c.toDocument());
					contributors.add(c.getCommitter());
				}
				commitHandler.insertMany(docs);
			}
			
			RepositoryDocumentHandler repoHandler = new RepositoryDocumentHandler();
			r.setContributors(new ArrayList<Contributor>(contributors));
			repoHandler.inser(r.toDocument());
			
			scm.close();
		}
	}

}
