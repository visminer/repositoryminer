package org.repositoryminer.persistence.dao;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;

/**
 * This class handles rm_working_tree collection operations.
 */
public class WorkingTreeDAO extends GenericDAO {

	private static final String COLLECTION_NAME = "rm_working_tree";

	public WorkingTreeDAO() {
		super(COLLECTION_NAME);
	}
	
	@Override
	public Document findById(String id, Bson projection) {
		return findOne(Filters.eq("_id", id), projection);
	}
	
}