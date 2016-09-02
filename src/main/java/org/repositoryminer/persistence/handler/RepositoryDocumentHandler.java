package org.repositoryminer.persistence.handler;

import org.bson.Document;
import org.repositoryminer.persistence.Connection;

public class RepositoryDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "rm_repositories";

	public RepositoryDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}

	public boolean checkIfRepositoryExists(String id) {
		Document clause = new Document("_id", id);
		return collection.count(clause) == 1 ? true : false;
	}
	
	public Document findOnlyContributors(String id) {
		Document projection = new Document("contributors", 1);
		return findById(id, projection);
	}

	public void updateOnlyContributors(Document doc) {
		Document clause = new Document("_id", doc.get("_id"));
		Document newDoc = new Document("$set", new Document("contributors", doc.get("contributors")));
		collection.updateOne(clause, newDoc);
	}
	
}