package org.repositoryminer.metrics.persistence;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.repositoryminer.persistence.GenericDAO;

import com.mongodb.client.model.Filters;

public class CodeAnalysisConfigDAO extends GenericDAO {

	private static final String COLLECTION_NAME = "rm_code_analysis_config";

	public CodeAnalysisConfigDAO() {
		super(COLLECTION_NAME);
	}

	public Document findByCommitHash(String hash, Bson projection) {
		return findOne(Filters.eq("commit", hash), projection);
	}
	
	public void deleteById(ObjectId id) {
		deleteOne(Filters.eq("_id", id));
	}
	
}