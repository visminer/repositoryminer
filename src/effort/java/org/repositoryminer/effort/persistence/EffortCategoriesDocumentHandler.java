package org.repositoryminer.effort.persistence;

import org.bson.Document;
import org.repositoryminer.persistence.Connection;
import org.repositoryminer.persistence.handler.DocumentHandler;

import com.mongodb.BasicDBObject;

public class EffortCategoriesDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "effort_categories_by_reference";
	
	public EffortCategoriesDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}

	public Document getByReference(String referenceId) {
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("reference", referenceId);
		
		return findOne(whereClause, null);
	}

	public Document getByReferenceName(String referenceName) {
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("reference_name", referenceName);
		
		return findOne(whereClause, null);
	}

}
