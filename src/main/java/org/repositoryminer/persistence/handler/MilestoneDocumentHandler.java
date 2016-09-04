package org.repositoryminer.persistence.handler;

import org.repositoryminer.persistence.Connection;

import com.mongodb.BasicDBObject;

public class MilestoneDocumentHandler extends DocumentHandler{

	private static final String COLLECTION_NAME = "rm_milestones";

	public MilestoneDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}
	
	public void deleteByRepository(String id) {
		BasicDBObject where = new BasicDBObject("repository", id);
		deleteMany(where);
	}
	
}