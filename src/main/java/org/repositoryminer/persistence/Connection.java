package org.repositoryminer.persistence;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;

public class Connection {

	private static MongoClient client;

	private String dbName;

	private Connection() {
	}

	private static class LazyHelper {
		private static final Connection INSTANCE = new Connection();
	}

	public static Connection getInstance() {
		return LazyHelper.INSTANCE;
	}

	public void connect(String uri, String dbName) {
		MongoClientURI mongoURI = new MongoClientURI(uri);
		client = new MongoClient(mongoURI);
		this.dbName = dbName;
	}

	public MongoCollection<Document> getCollection(String collName) {
		return client.getDatabase(dbName).getCollection(collName);
	}

	public void close() {
		client.close();
	}

}