package org.repositoryminer.technicaldebt.persistence;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.repositoryminer.persistence.GenericDAO;

import com.mongodb.client.model.Filters;

public class TechnicalDebtConfigDAO extends GenericDAO {

	private static final String COLLECTION_NAME = "rm_technical_debt_config";

	public TechnicalDebtConfigDAO() {
		super(COLLECTION_NAME);
	}

	public Document findByCommitHash(String hash, Bson projection) {
		return findOne(Filters.eq("commit", hash), projection);
	}
	
	public void deleteById(ObjectId id) {
		deleteOne(Filters.eq("_id", id));
	}
	
}
