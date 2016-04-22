package org.repositoryminer.persistence.handler;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.repositoryminer.persistence.Connection;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;

import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.include;

public class CommitDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "commits";

	public CommitDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}

	public List<Document> getAllByRepository(String repository) {
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("repository", repository);
		return findMany(whereClause, null);
	}

	@SuppressWarnings("unchecked")
	public List<Document> getAllDiffs(String id) {
		Bson projection = fields(include("diffs"), excludeId());
		Document commit = findById(id, projection);
		List<Document> diffs = (List<Document>) commit.get("diffs");
		return diffs;
	}

	public List<Document> getAllByDateRange(String repository, Date fromDate, Date toDate, boolean isCommitter) {
		BasicDBObject query = new BasicDBObject();
		String field = isCommitter ? "commit_date" : "authored_date";
		query.put(field, BasicDBObjectBuilder.start("$gte", fromDate).add("$lte", toDate).get());
		return findMany(query, null);
	}

	public List<Document> getAllBeforeDate(String idRepository, Date date, boolean isCommitter) {
		BasicDBObject query = new BasicDBObject();
		String field = isCommitter ? "commit_date" : "authored_date";
		query.put(field, new BasicDBObject("$lt", date));
		return findMany(query, null);
	}

	public List<Document> getAllAfterDate(String idRepository, Date date, boolean isCommitter) {
		BasicDBObject query = new BasicDBObject();
		String field = isCommitter ? "commit_date" : "authored_date";
		query.put(field, BasicDBObjectBuilder.start("$gt", date).get());
		return findMany(query, null);
	}

}