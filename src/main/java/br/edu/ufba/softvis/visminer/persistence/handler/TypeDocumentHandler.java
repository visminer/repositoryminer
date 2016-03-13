package br.edu.ufba.softvis.visminer.persistence.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;

public class TypeDocumentHandler extends DocumentHandler{
	
	private static final String COLLECTION_NAME = "types";
	
	public Document getOneById(String uid, String idCommit) {
		BasicDBObject andQuery = new BasicDBObject();
		List<BasicDBObject> conditions = new ArrayList<BasicDBObject>();
		
		conditions.add(new BasicDBObject("commit", idCommit));
		conditions.add(new BasicDBObject("type.uid", uid));
		andQuery.put("$and", conditions);
		
	   return findOne(COLLECTION_NAME, andQuery);
	}
	
	@SuppressWarnings("unchecked")
	public List<Document> getAntiPatterns(String uid, String idCommit) {
		Document type = getOneById(uid, idCommit);
		Object antiPatterns = type.get("antipatterns");
		if (antiPatterns instanceof ArrayList<?>) {
			return ((ArrayList<Document>) type.get("antipatterns"));
		}
		return Collections.emptyList();
	}
	
	public List<Document> getAllClassesAndInterfaces(String idCommit) {
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("commit", idCommit);
		
		return findWhere(COLLECTION_NAME, whereClause);
	}
	
}
