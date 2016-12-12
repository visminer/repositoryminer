package org.repositoryminer.persistence.handler;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.repositoryminer.persistence.Connection;

import com.mongodb.BasicDBObject;

public class SnapshotAnalysisDocumentHandler extends DocumentHandler{

	private static final String COLLECTION_NAME = "rm_snapshot_analysis";
	
	public SnapshotAnalysisDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}

	public void deleteByCommits(String repositoryId, List<String> commitsToRemove) {
		List<BasicDBObject> where = new ArrayList<BasicDBObject>(2);
		where.add(new BasicDBObject("repository", new ObjectId(repositoryId)));
		where.add(new BasicDBObject("commit", new BasicDBObject("$in", commitsToRemove)));
		deleteMany(new BasicDBObject("$and", where));
	}
	
}