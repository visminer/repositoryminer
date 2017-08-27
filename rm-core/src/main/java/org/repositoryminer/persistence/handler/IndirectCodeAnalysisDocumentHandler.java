package org.repositoryminer.persistence.handler;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.repositoryminer.persistence.Connection;
import org.repositoryminer.scm.ReferenceType;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;

public class IndirectCodeAnalysisDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "rm_indirect_code_analysis";

	public IndirectCodeAnalysisDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}

	public boolean hasRecord(String repositoryId, String commit) {
		List<BasicDBObject> where = new ArrayList<BasicDBObject>(2);
		where.add(new BasicDBObject("repository", new ObjectId(repositoryId)));
		where.add(new BasicDBObject("commit", commit));

		return collection.count(new BasicDBObject("$and", where)) > 0 ? true : false;
	}

	public Document find(String repositoryId, String commit, Bson projection) {
		List<BasicDBObject> where = new ArrayList<BasicDBObject>(2);
		where.add(new BasicDBObject("repository", new ObjectId(repositoryId)));
		where.add(new BasicDBObject("commit", commit));
		
		return findOne(new BasicDBObject("$and", where), projection);
	}

	public void updateOnlyReference(Document doc) {
		
	}

	public void updateOnlyReference(ObjectId id, String name, ReferenceType type) {
		Document clause = new Document("_id", id);
		Document newDoc = new Document("$set", new Document("reference_name", name).append("reference_type", type.toString()));
		collection.updateOne(clause, newDoc);
	}

	public Document findByFileAndSnapshot(long filehash, String snapshot, Bson projection) {
		Bson clause1 = new BasicDBObject("filehash", filehash);
		Bson clause2 = new BasicDBObject("commit", snapshot);
		return findOne(Filters.and(clause1, clause2), projection);
	}
	
}