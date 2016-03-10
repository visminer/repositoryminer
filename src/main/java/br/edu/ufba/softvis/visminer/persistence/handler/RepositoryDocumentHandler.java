package br.edu.ufba.softvis.visminer.persistence.handler;

import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;

public class RepositoryDocumentHandler extends DocumentHandler{
	
	private static final String COLLECTION_NAME = "repositories";
	
	public Document getOneById(String uid) {
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("uid", uid);
		
	   return findOne(COLLECTION_NAME, whereClause);
	}
	
	public List<Document> getAll() {		
		return findAll(COLLECTION_NAME);
	}
	
}
