package org.repositoryminer.persistence.handler;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.repositoryminer.persistence.Connection;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;

public class CommitDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "rm_commits";

	public CommitDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}

	@Override
	public Document findById(String id, Bson projection) {
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("_id", id);

		return findOne(whereClause, projection);
	}

	public List<Document> findByIdColl(String repository, List<String> ids, Bson projection) {
		List<BasicDBObject> where = new ArrayList<BasicDBObject>(2);
		where.add(new BasicDBObject("repository", new ObjectId(repository)));
		where.add(new BasicDBObject("_id", new BasicDBObject("$in", ids)));

		MongoCursor<Document> cursor = collection.find(new BasicDBObject("$and", where))
				.sort(new BasicDBObject("commit_date", 1)).projection(projection).iterator();

		return fromCursorToList(cursor);
	}

}