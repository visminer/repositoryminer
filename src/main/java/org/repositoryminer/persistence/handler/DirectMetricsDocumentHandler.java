package org.repositoryminer.persistence.handler;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.persistence.Connection;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Projections;

public class DirectMetricsDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "rm_direct_metrics";

	public DirectMetricsDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}

	public Document getMetricsMeasures(long fileHash, String commitHash) {
		return getMeasuresHelper(fileHash, commitHash, "abstract_types.metrics");
	}

	public Document getCodeSmellsMeasures(long fileHash, String commitHash) {
		return getMeasuresHelper(fileHash, commitHash, "abstract_types.codesmells");
	}

	private Document getMeasuresHelper(long fileHash, String commit, String field) {
		List<BasicDBObject> conditions = new ArrayList<BasicDBObject>();
		conditions.add(new BasicDBObject("filehash", fileHash));
		conditions.add(new BasicDBObject("commit", commit));
		return findOne(new BasicDBObject("$and", conditions), Projections.include(field));
	}

}