package org.visminer.main;

import org.visminer.extractor.PersistRepository;
import org.visminer.model.business.Repository;
import org.visminer.persistence.PersistenceFacade;

public class Analyzer {

	private Repository repository;
	
	public Analyzer(){
		this.repository = new Repository();
	}
	
	public void setRepository(Repository repository){
		this.repository = repository;
	}
	
	public Analyzer setRepositoryPath(String path){
		this.repository.setPath(path);
		return this;
	}
	
	public Analyzer setRepositoryName(String name){
		this.repository.setName(name);
		return this;
	}
	
	public Analyzer setRepositoryType(int type){
		this.repository.setType(type);
		return this;
	}
	
	public Analyzer setRepositoryRemoteOwner(String owner){
		this.repository.setRemote_owner(owner);
		return this;
	}
	
	public Analyzer setRepositoryRemoteName(String name){
		this.repository.setRemote_name(name);
		return this;
	}
	
	public Analyzer setRepositoryRemoteType(int type){
		this.repository.setRemoteType(type);
		return this;
	}

	public Repository analyzeRepository(){
		
		PersistenceFacade persistenceFacade = new PersistenceFacade();
		
		String path = repository.getPath();
		org.visminer.model.database.Repository repo = persistenceFacade.getRepositoryByPath(path);
		
		if(repo == null){
			PersistRepository persist = new PersistRepository(repository);
			persist.persist();
			repo = persistenceFacade.getRepositoryByPath(path);
		}else{
			/* TODO: else update repository */
		}
	
		return new Repository(repo.getId(), repo.getPath(), repo.getName(), repo.getRemoteOwner(),
				repo.getRemoteName(), repo.getType(), repo.getRemoteService());
		
	}
	
}