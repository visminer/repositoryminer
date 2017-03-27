package org.repositoryminer.persistence.handler;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.repositoryminer.persistence.Connection;
import org.repositoryminer.scm.ReferenceType;

import com.mongodb.BasicDBObject;

public class ReferenceDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "rm_references";

	public ReferenceDocumentHandler(){
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}
	
	public Document findByPath(String path, String repositoryId, Bson projection) {
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("path", path);
		whereClause.put("repository", new ObjectId(repositoryId));
		return findOne(whereClause, projection);
	}

	public List<Document> getByRepository(String repository) {
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("repository", new ObjectId(repository));
		return findMany(whereClause, null);
	}

	public Document findByNameAndType(String name, ReferenceType type, String repositoryId, Bson projection) {
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("name", name);
		whereClause.put("type", type.toString());
		whereClause.put("repository", new ObjectId(repositoryId));
		return findOne(whereClause, projection);
	}
	
	public void updateOnlyCommits(String id, List<String> commits) {
		Document clause = new Document("_id", new ObjectId(id));
		Document newDoc = new Document("$set", new Document("commits", commits));
		collection.updateOne(clause, newDoc);
	}
	
}