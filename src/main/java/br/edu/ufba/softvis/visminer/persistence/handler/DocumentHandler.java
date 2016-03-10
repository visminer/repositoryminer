package br.edu.ufba.softvis.visminer.persistence.handler;

import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import br.edu.ufba.softvis.visminer.persistence.Database;

public abstract class DocumentHandler {

	protected void save(String collectionName, Document document) {
		Database.getInstance().insert(collectionName, document);
	}
	
	protected Document findOne(String collectionName, BasicDBObject whereClause) {
		 List<Document> documents = Database.getInstance().findWhere(collectionName, whereClause);
		 if (documents.isEmpty()) {
			 return null;
		 } 
		 return documents.get(0);
	}
	
	protected List<Document> findWhere(String collectionName, BasicDBObject whereClause) {
		return Database.getInstance().findWhere(collectionName, whereClause);
	}
	
	protected List<Document> findAll(String collectionName) {
		return Database.getInstance().findAll(collectionName);
	}
	
	protected DeleteResult deleteOne(String collectionName, BasicDBObject whereClause) {
		return Database.getInstance().deleteOne(collectionName, whereClause);
	}
	
	protected DeleteResult deleteMany(String collectionName, BasicDBObject whereClause) {
		return Database.getInstance().deleteMany(collectionName, whereClause);
	}
	
	protected DeleteResult deleteAll(String collectionName) {
		return Database.getInstance().deleteMany(collectionName, new BasicDBObject());
	}	
	
	protected UpdateResult updateOne(String collectionName, BasicDBObject searchQuery, BasicDBObject newDocument) {
		return Database.getInstance().updateOne(collectionName, searchQuery, newDocument);
	}
	
	protected UpdateResult updateMany(String collectionName, BasicDBObject searchQuery, BasicDBObject newDocument) {
		return Database.getInstance().updateMany(collectionName, searchQuery, newDocument);
	}
	
	protected UpdateResult updateAll(String collectionName, BasicDBObject newDocument) {
		return Database.getInstance().updateMany(collectionName, new BasicDBObject(), newDocument);
	}
}
