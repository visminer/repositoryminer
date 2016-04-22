package org.repositoryminer.persistence;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Connection {

	private static Connection conn;

	private MongoClient client;
	private MongoDatabase database;

	private Connection() {
	}

	synchronized public static Connection getInstance() {
		if (conn == null)
			conn = new Connection();
		return conn;
	}

	public void connect(String uri, String dbName) {
		MongoClientURI mongoURI = new MongoClientURI(uri);
		client = new MongoClient(mongoURI);
		database = client.getDatabase(dbName);
	}

	public MongoDatabase getDatabase() {
		return database;
	}

	public MongoCollection<Document> getCollection(String collName) {
		return database.getCollection(collName);
	}

	public void close() {
		client.close();
	}

}