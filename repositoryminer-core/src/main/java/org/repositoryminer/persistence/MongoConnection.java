package org.repositoryminer.persistence;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;

/**
 * This class handles MongoDB database connection.
 */
public class MongoConnection {

	private static class LazyHelper {
		private static final MongoConnection INSTANCE = new MongoConnection();
	}

	private static MongoClient client;
	private String database;

	private MongoConnection() {
	}

	/**
	 * @return a shared instance of connection handler.
	 */
	public static MongoConnection getInstance() {
		return LazyHelper.INSTANCE;
	}

	/**
	 * Opens database connection.
	 * 
	 * @param uri
	 *            the database URI.
	 * @param database
	 *            the database name.
	 */
	public void connect(String uri, String database) {
		MongoConnection.client = new MongoClient(new MongoClientURI(uri));
		this.database = database;
	}

	/**
	 * Returns a database collection handler.
	 * 
	 * @param collection
	 *            the collection name.
	 * @return the collection handler.
	 */
	public MongoCollection<Document> getCollection(String collection) {
		return MongoConnection.client.getDatabase(database).getCollection(collection);
	}

	/**
	 * Closes database connection.
	 */
	public void close() {
		MongoConnection.client.close();
	}

}