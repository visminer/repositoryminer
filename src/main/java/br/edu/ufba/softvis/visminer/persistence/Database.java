package br.edu.ufba.softvis.visminer.persistence;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import br.edu.ufba.softvis.visminer.config.DBConfig;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * 
 * Management of database connection.
 */
public class Database {

	private MongoDatabase mongodb = null;
	
	private static Database instance = null;
	private Database(){}
	public static Database getInstance(){
		if(instance == null){
			instance = new Database();
		}
		return instance;
	}
	
	public void prepare(DBConfig dbConfig){
		mongodb = dbConfig.configDB();
	}
	
	public MongoDatabase getMongoDb() {
		return mongodb;
	}
	
	public MongoCollection<Document> getDbCollection(String collectionName) {
		MongoCollection<Document> coll = mongodb.getCollection(collectionName);
		
		return coll;
	}
	
	public void insert(String collection, Document document) {
		MongoCollection<Document> coll = getDbCollection(collection);		
		coll.insertOne(document);
	}
	
	public List<Document> findWhere(String collection, BasicDBObject whereClause) {	
		MongoCursor<Document> cursor = getDbCollection(collection).find(whereClause).iterator();
		return fromCursorToList(cursor);		
	}
	
	public List<Document> findAll(String collection) {
		MongoCursor<Document> cursor = getDbCollection(collection).find().iterator();
		return fromCursorToList(cursor);		
	}
	
	private List<Document> fromCursorToList(MongoCursor<Document> cursor) {
		List<Document> list = new ArrayList<Document>();
		
		while (cursor.hasNext()) {
			list.add(cursor.next());
		}		
		return list;
	}
	
	public DeleteResult deleteOne(String collectionName, BasicDBObject whereClause) {
		return getDbCollection(collectionName).deleteOne(whereClause);
	}
	
	public DeleteResult deleteMany(String collectionName, BasicDBObject whereClause) {
		return getDbCollection(collectionName).deleteMany(whereClause);
	}
	
	public DeleteResult deleteAll(String collectionName) {
		return getDbCollection(collectionName).deleteMany(new BasicDBObject());
	}	
	
	public UpdateResult updateOne(String collectionName, BasicDBObject searchQuery, BasicDBObject newDocument) {
		return getDbCollection(collectionName).updateOne(searchQuery, newDocument);
	}
	
	public UpdateResult updateMany(String collectionName, BasicDBObject searchQuery, BasicDBObject newDocument) {
		return getDbCollection(collectionName).updateMany(searchQuery, newDocument);
	}
	
	public UpdateResult updateAll(String collectionName, BasicDBObject newDocument) {
		return getDbCollection(collectionName).updateMany(new BasicDBObject(), newDocument);
	}
}