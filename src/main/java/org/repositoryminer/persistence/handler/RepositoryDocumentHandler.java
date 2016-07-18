package org.repositoryminer.persistence.handler;

import org.bson.Document;
import org.repositoryminer.persistence.Connection;

public class RepositoryDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "repositories";

	public RepositoryDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}

	public boolean checkIfRepositoryExists(String id) {
		Document clause = new Document("_id", id);
		return collection.count(clause) == 1 ? true : false;
	}
	
}