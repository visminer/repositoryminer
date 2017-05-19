package org.repositoryminer.remoteminer.persistence;

import org.bson.types.ObjectId;
import org.repositoryminer.persistence.Connection;
import org.repositoryminer.persistence.handler.DocumentHandler;

import com.mongodb.BasicDBObject;

public class IssueDocumentHandler extends DocumentHandler{

	private static final String COLLECTION_NAME = "rm_issues";

	public IssueDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}
	
	public void deleteByRepository(String id) {
		BasicDBObject where = new BasicDBObject("repository", new ObjectId(id));
		deleteMany(where);
	}
	
}