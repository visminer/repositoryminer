package org.repositoryminer.plugin;

import org.bson.types.ObjectId;
import org.repositoryminer.RepositoryMiner;
import org.repositoryminer.RepositoryMinerException;
import org.repositoryminer.persistence.RepositoryDAO;

import com.mongodb.client.model.Projections;

public abstract class MiningPlugin<T> {

	protected ObjectId repositoryId;
	
	public MiningPlugin(String repositoryKey) {
		RepositoryDAO dao = new RepositoryDAO();
		repositoryId = dao.findByKey(repositoryKey, Projections.include("_id")).getObjectId("_id");
		
		if (repositoryId == null) {
			throw new RepositoryMinerException("Repository not found.");
		}
	}
	
	/**
	 * This analysis must to execute after mining the repository with {@link RepositoryMiner}.
	 * 
	 * @param config
	 */
	public abstract void mine(T config);
	
}