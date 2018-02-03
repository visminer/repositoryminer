package org.repositoryminer.persistence;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

/**
 * This class handles a generic collection.
 */
public class GenericDAO {

	protected MongoCollection<Document> collection;

	/**
	 * @param collectionName
	 *            the target collection name.
	 */
	public GenericDAO(String collectionName) {
		collection = MongoConnection.getInstance().getCollection(collectionName);
	}

	/**
	 * Count the number of documents.
	 * 
	 * @param where
	 *            clause to filter results or null for no filter.
	 * @return the number of documents.
	 */
	public long count(Bson where) {
		return collection.count(where);
	}

	/**
	 * Inserts one document.
	 * 
	 * @param document
	 *            the document to be stored.
	 */
	public void insert(Document document) {
		collection.insertOne(document);
	}

	/**
	 * Inserts various documents.
	 * 
	 * @param documents
	 *            list of documents to be stored.
	 */
	public void insertMany(List<Document> documents) {
		collection.insertMany(documents);
	}

	/**
	 * Updates one document.
	 * 
	 * @param where
	 *            clause to filter results or null for no filter.
	 * @param newDocument
	 *            the modifications.
	 * @return the result of update operation.
	 */
	public UpdateResult updateOne(Bson where, Bson newDocument) {
		return collection.updateOne(where, newDocument);
	}

	/**
	 * Updates various documents.
	 * 
	 * @param where
	 *            clause to filter results or null for no filter.
	 * @param newDocument
	 *            the modifications.
	 * @return the result of update operation.
	 */
	public UpdateResult updateMany(Bson where, Bson newDocument) {
		return collection.updateMany(where, newDocument);
	}

	/**
	 * Deletes one document.
	 * 
	 * @param where
	 *            clause to filter results or null for no filter.
	 * @return the result of delete operation.
	 */
	public DeleteResult deleteOne(Bson where) {
		return collection.deleteOne(where);
	}

	/**
	 * Deletes various documents.
	 * 
	 * @param where
	 *            clause to filter results or null for no filter.
	 * @return the result of delete operation.
	 */
	public DeleteResult deleteMany(Bson whereClause) {
		return collection.deleteMany(whereClause);
	}

	/**
	 * Retrieves one document by its id.
	 * 
	 * @param id
	 *            the document id.
	 * @param projection
	 *            the query projection.
	 * @return the found document or null.
	 */
	public Document findById(String id, Bson projection) {
		return findOne(Filters.eq("_id", new ObjectId(id)), projection);
	}
	
	/**
	 * Retrieves one document by its id.
	 * 
	 * @param id
	 *            the document id.
	 * @param projection
	 *            the query projection.
	 * @return the found document or null.
	 */
	public Document findById(ObjectId id, Bson projection) {
		return findOne(Filters.eq("_id", id), projection);
	}

	/**
	 * Retrieves only the first document from a query.
	 * 
	 * @param where
	 *            clause to filter results or null for no filter.
	 * @param projection
	 *            the query projection.
	 * @return the found document or null.
	 */
	public Document findOne(Bson where, Bson projection) {
		return collection.find(where).projection(projection).first();
	}

	/**
	 * Retrieves various documents.
	 * 
	 * @param where
	 *            clause to filter results or null for no filter.
	 * @param projection
	 *            the query projection.
	 * @return a list of documents.
	 */
	public List<Document> findMany(Bson where, Bson projection) {
		return fromCursorToList(collection.find(where).projection(projection).iterator());
	}

	/**
	 * Converts a query cursor to a list.
	 * 
	 * @param cursor
	 *            the query cursor.
	 * @return a list of documents.
	 */
	public List<Document> fromCursorToList(MongoCursor<Document> cursor) {
		List<Document> list = new ArrayList<Document>();

		while (cursor.hasNext()) {
			list.add(cursor.next());
		}

		cursor.close();
		return list;
	}

}