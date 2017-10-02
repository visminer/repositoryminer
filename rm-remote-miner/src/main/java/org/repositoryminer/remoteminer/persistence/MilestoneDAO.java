package org.repositoryminer.remoteminer.persistence;

import org.bson.types.ObjectId;
import org.repositoryminer.persistence.dao.GenericDAO;

import com.mongodb.BasicDBObject;

public class MilestoneDAO extends GenericDAO {

	private static final String COLLECTION_NAME = "rm_milestones";

	public MilestoneDAO() {
		super(COLLECTION_NAME);
	}
	
	public void deleteByRepository(String id) {
		BasicDBObject where = new BasicDBObject("repository", new ObjectId(id));
		deleteMany(where);
	}
	
}