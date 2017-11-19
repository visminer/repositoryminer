package org.repositoryminer.technicaldebt.persistence;

import org.repositoryminer.persistence.GenericDAO;

import com.mongodb.client.model.Filters;

public class TechnicalDebtDAO extends GenericDAO {

	private static final String COLLECTION_NAME = "rm_technical_debt";

	public TechnicalDebtDAO() {
		super(COLLECTION_NAME);
	}

	public void deleteByCommit(String hash) {
		deleteMany(Filters.eq("commit", hash));
	}
	
}