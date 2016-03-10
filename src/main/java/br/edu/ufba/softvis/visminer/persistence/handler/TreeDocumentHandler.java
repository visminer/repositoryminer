package br.edu.ufba.softvis.visminer.persistence.handler;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;

import br.edu.ufba.softvis.visminer.constant.TreeType;

public class TreeDocumentHandler extends DocumentHandler{
	
	private static final String COLLECTION_NAME = "trees";
	
	public Document getOneById(String uid) {
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("uid", uid);
		
	   return findOne(COLLECTION_NAME, whereClause);
	}
	
	public List<Document> getAllByRepository(String idRepository) {
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("repository", idRepository);
		
		return findWhere(COLLECTION_NAME, whereClause);
	}
	
	public List<Document> getBranchesByRepository(String idRepository) {
		BasicDBObject query = getTreesByTypeAndRepository(idRepository, TreeType.BRANCH);		
		return findWhere(COLLECTION_NAME, query);
	}
	
	public List<Document> getTagsByRepository(String idRepository) {
		BasicDBObject query = getTreesByTypeAndRepository(idRepository, TreeType.TAG);		
		return findWhere(COLLECTION_NAME, query);
	}
	
	private BasicDBObject getTreesByTypeAndRepository(String idRepository, TreeType type) {
		BasicDBObject andQuery = new BasicDBObject();
		List<BasicDBObject> conditions = new ArrayList<BasicDBObject>();
		
		conditions.add(new BasicDBObject("repository", idRepository));
		conditions.add(new BasicDBObject("type", type));
		andQuery.put("$and", conditions);
		return andQuery;
	}
}
