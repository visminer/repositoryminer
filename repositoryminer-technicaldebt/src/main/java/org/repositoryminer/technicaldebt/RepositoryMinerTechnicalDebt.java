package org.repositoryminer.technicaldebt;

import java.io.IOException;
import java.util.Set;

import org.bson.Document;
import org.repositoryminer.RepositoryMinerException;
import org.repositoryminer.persistence.RepositoryDAO;
import org.repositoryminer.plugin.SnapshotAnalysisPlugin;
import org.repositoryminer.technicaldebt.model.TDIndicator;

import com.mongodb.client.model.Projections;

public class RepositoryMinerTechnicalDebt extends SnapshotAnalysisPlugin<Set<TDIndicator>>{

	@Override
	public void init(String repositoryKey) throws IOException {
		Document repoDoc = new RepositoryDAO().findByKey(repositoryKey, Projections.include("_id"));
		if (repoDoc == null) {
			throw new RepositoryMinerException("Repository with the key " + repositoryKey + " does not exists");
		}
		
		repositoryId = repoDoc.getObjectId("_id");
	}

	@Override
	public void run(String snapshot, Set<TDIndicator> config) {
		System.out.println("Plugin in development.");
	}
	
	@Override
	public void finish() throws IOException {
		// NOTHING IS NEEDED HERE
	}

}