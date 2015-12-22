package br.edu.ufba.softvis.visminer.persistence;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.config.DBConfig;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

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
}