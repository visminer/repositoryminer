package org.repositoryminer.persistence.handler;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.persistence.Connection;

import com.mongodb.BasicDBObject;

public class ReferenceDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "rm_references";

	public ReferenceDocumentHandler(){
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}
	
	public Document findByPathAndName(String path, String name) {
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("path", path);
		whereClause.put("name", name);

		return findOne(whereClause);
	}

	public List<Document> getByRepository(String repository) {
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("repository", new ObjectId(repository));
		return findMany(whereClause, null);
	}

}