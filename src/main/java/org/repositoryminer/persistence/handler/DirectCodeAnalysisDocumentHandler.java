package org.repositoryminer.persistence.handler;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.repositoryminer.persistence.Connection;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

public class DirectCodeAnalysisDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "rm_direct_code_analysis";

	public DirectCodeAnalysisDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}

	public Document getMetrics(long fileHash, String commitHash) {
		return findByFileAndCommit(fileHash, commitHash, Projections.include("classes.metrics"));
	}

	public Document getCodeSmells(long fileHash, String commitHash) {
		return findByFileAndCommit(fileHash, commitHash, Projections.include("classes.codesmells"));
	}

	public Document findByFileAndCommit(long fileHash, String commit, Bson projection) {
		Bson clause1 = new BasicDBObject("filehash", fileHash);
		Bson clause2 = new BasicDBObject("commit", commit);
		return findOne(Filters.and(clause1, clause2), projection);
	}
	
}