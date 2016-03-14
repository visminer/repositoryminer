package br.edu.ufba.softvis.visminer.persistence.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;

public class CommitDocumentHandler extends DocumentHandler {	
	
	private static final String COLLECTION_NAME = "commits";
	
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
	
	
	@SuppressWarnings("unchecked")
	public List<Document> getAllFiles(String idCommit) {
		Document commit = getOneById(idCommit);
		Object files = commit.get("files");
		if (files instanceof ArrayList<?>) {
			return ((ArrayList<Document>) commit.get("files"));
		}
		return Collections.emptyList();
	}
	
	public List<Document> getAllByDateRange(String idRepository, Date fromDate, Date toDate ){
		BasicDBObject query = new BasicDBObject();
		query.put("date", BasicDBObjectBuilder.start("$gte", fromDate).add("$lte", toDate).get());
		
		return findWhere(COLLECTION_NAME, query);
	}
	
	public List<Document> getAllBeforeDate(String idRepository, Date date){
		BasicDBObject query = new BasicDBObject();
		query.put("date", new BasicDBObject("$lt", date));
		
		return findWhere(COLLECTION_NAME, query);
	}
	
	public List<Document> getAllAfterDate(String idRepository, Date date){
		BasicDBObject query = new BasicDBObject();
		query.put("date", BasicDBObjectBuilder.start("$gt", date).get());
		
		return findWhere(COLLECTION_NAME, query);
	}

}
