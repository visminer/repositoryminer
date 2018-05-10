package org.repositoryminer.plugin;

import org.bson.Document;
import org.repositoryminer.RepositoryMiner;
import org.repositoryminer.RepositoryMinerException;
import org.repositoryminer.domain.Repository;
import org.repositoryminer.persistence.RepositoryDAO;

public abstract class MiningPlugin<T> {

	protected Repository repository;
	
	public MiningPlugin(String repositoryKey) {
		Document repoDoc = new RepositoryDAO().findByKey(repositoryKey, null);
		
		if (repoDoc == null) {
			throw new RepositoryMinerException("Repository not found.");
		}
		
		repository = Repository.parseDocument(repoDoc);
	}
	
	/**
	 * This analysis must to execute after mining the repository with {@link RepositoryMiner}.
	 * 
	 * @param config
	 */
	public abstract void mine(T config);
	
}