package org.repositoryminer.persistence.handler;

import org.repositoryminer.persistence.Connection;

public class CommitAnalysisDocumentHandler extends DocumentHandler{

	private static final String COLLECTION_NAME = "commit_analysis";
	
	public CommitAnalysisDocumentHandler(){
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}
	
}