package org.repositoryminer.persistence.handler;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.persistence.Connection;

import com.mongodb.BasicDBObject;

public class RepositoryDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "rm_repositories";

	public RepositoryDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}

	public boolean checkIfRepositoryExistsById(String name) {
		Document clause = new Document("name", name);
		return collection.count(clause) > 0 ? true : false;
	}

	public Document findRepositoryByName(String name) {
		return findOne(new BasicDBObject("name", name));
	}

	public Document findOnlyContributors(String id) {
		Document projection = new Document("contributors", 1);
		return findById(id, projection);
	}

	public void updateOnlyContributors(String id, List<Document> contributors) {
		Document clause = new Document("_id", new ObjectId(id));
		Document newDoc = new Document("$set", new Document("contributors", contributors));
		collection.updateOne(clause, newDoc);
	}
	
}