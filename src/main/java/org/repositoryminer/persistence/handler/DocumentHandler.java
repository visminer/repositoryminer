package org.repositoryminer.persistence.handler;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class DocumentHandler {

	protected MongoCollection<Document> collection = null;

	public void insert(Document document) {
		collection.insertOne(document);
	}

	public void insertMany(List<Document> documents) {
		collection.insertMany(documents);
	}

	public Document findById(String id, Bson projection){
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("_id", new ObjectId(id));
		return findOne(whereClause, projection);
	}
	
	public Document findById(String id){
		return findById(id, null);
	}

	public Document findOne(BasicDBObject whereClause) {
		Document doc = collection.find(whereClause).first();
		
		return doc;
	}

	public Document findOne(BasicDBObject whereClause, Bson projection) {
		Document doc = collection.find(whereClause).projection(projection).first();
		
		return doc;
	}

	public List<Document> findAll() {
		MongoCursor<Document> cursor = collection.find().iterator();
		
		return fromCursorToList(cursor);
	}

	public List<Document> findAll(Bson projection) {
		MongoCursor<Document> cursor = collection.find().projection(projection).iterator();
		
		return fromCursorToList(cursor);
	}

	public List<Document> findMany(BasicDBObject whereClause) {
		MongoCursor<Document> cursor = collection.find(whereClause).iterator();
		
		return fromCursorToList(cursor);
	}

	public List<Document> findMany(BasicDBObject whereClause, Bson projection) {
		MongoCursor<Document> cursor = collection.find(whereClause).projection(projection).iterator();
		
		return fromCursorToList(cursor);
	}

	public List<Document> fromCursorToList(MongoCursor<Document> cursor) {
		List<Document> list = new ArrayList<Document>();

		while (cursor.hasNext())
			list.add(cursor.next());

		cursor.close();
		
		return list;
	}

	public DeleteResult deleteOne(BasicDBObject whereClause) {
		return collection.deleteOne(whereClause);
	}

	public DeleteResult deleteMany(BasicDBObject whereClause) {
		return collection.deleteMany(whereClause);
	}

	public DeleteResult deleteAll() {
		return collection.deleteMany(new BasicDBObject());
	}

	public UpdateResult updateOne(BasicDBObject searchQuery, BasicDBObject newDocument) {
		return collection.updateOne(searchQuery, newDocument);
	}

	public UpdateResult updateMany(BasicDBObject searchQuery, BasicDBObject newDocument) {
		return collection.updateMany(searchQuery, newDocument);
	}

	public UpdateResult updateAll(BasicDBObject newDocument) {
		return collection.updateMany(new BasicDBObject(), newDocument);
	}

}