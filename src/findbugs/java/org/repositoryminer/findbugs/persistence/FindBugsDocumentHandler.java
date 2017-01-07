package org.repositoryminer.findbugs.persistence;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.repositoryminer.persistence.Connection;
import org.repositoryminer.persistence.handler.DocumentHandler;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;

public class FindBugsDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "findbugs_bugs_analysis";
	
	public FindBugsDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}

	public Document findByClasses(String[] classes, String commit, Bson projection) {
		Bson clause1 = Filters.in("bugs.class", classes);
		Bson clause2 = new BasicDBObject("commit", commit);
		return findOne(Filters.and(clause1, clause2), projection);
	}
	
}