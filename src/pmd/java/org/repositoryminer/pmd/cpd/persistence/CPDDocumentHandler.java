package org.repositoryminer.pmd.cpd.persistence;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.repositoryminer.persistence.Connection;
import org.repositoryminer.persistence.handler.DocumentHandler;

import com.mongodb.BasicDBObject;

public class CPDDocumentHandler extends DocumentHandler{

	private static final String COLLECTION_NAME = "pmd_cpd_analysis";
	
	public CPDDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}

	public long countOccurrences(long filehash, String commit) {
		List<BasicDBObject> where = new ArrayList<BasicDBObject>(2);
		where.add(new BasicDBObject("files_info.filehash", filehash));
		where.add(new BasicDBObject("commit", commit));
		
		return collection.count(new BasicDBObject("$and", where));
	}
	
	public List<Document> findOccurrences(long filehash, String commit, Bson projection) {
		List<BasicDBObject> where = new ArrayList<BasicDBObject>(2);
		where.add(new BasicDBObject("files_info.filehash", filehash));
		where.add(new BasicDBObject("commit", commit));
		
		return findMany(new BasicDBObject("$and", where), projection);
	}
	
}