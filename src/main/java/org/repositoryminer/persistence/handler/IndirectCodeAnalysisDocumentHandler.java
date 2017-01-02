package org.repositoryminer.persistence.handler;

import org.repositoryminer.persistence.Connection;

public class IndirectCodeAnalysisDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "rm_indirect_code_analysis";
	
	public IndirectCodeAnalysisDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}
	
}