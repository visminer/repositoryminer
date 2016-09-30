package org.repositoryminer.persistence.handler;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.persistence.Connection;

import com.mongodb.BasicDBObject;

public class CommitAnalysisDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "rm_commits_analysis";

	public CommitAnalysisDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}

	public Document getAllMeasures(long fileHash, String commitHash) {
		List<BasicDBObject> conditions = new ArrayList<BasicDBObject>();
		conditions.add(new BasicDBObject("file_hash", fileHash));
		conditions.add(new BasicDBObject("commit", commitHash));
		return findOne(new BasicDBObject("$and", conditions), null);
	}

	public Document getMetricsMeasures(long fileHash, String commitHash) {
		return getMeasuresHelper(fileHash, commitHash, "abstract_types.codesmells", "abstract_types.technicaldebts");
	}

	public Document getCodeSmellsMeasures(long fileHash, String commitHash) {
		return getMeasuresHelper(fileHash, commitHash, "abstract_types.metrics", "abstract_types.technicaldebts");
	}

	public Document getTechnicalDebtsMeasures(long fileHash, String commitHash) {
		return getMeasuresHelper(fileHash, commitHash, "abstract_types.metrics", "abstract_types.codesmells");
	}

	private Document getMeasuresHelper(long fileHash, String commit, String nullField1, String nullField2) {
		BasicDBObject fields = new BasicDBObject();
		fields.put(nullField1, 0);
		fields.put(nullField2, 0);

		List<BasicDBObject> conditions = new ArrayList<BasicDBObject>();
		conditions.add(new BasicDBObject("file_hash", fileHash));
		conditions.add(new BasicDBObject("commit", commit));
		return findOne(new BasicDBObject("$and", conditions), fields);
	}

}