package org.repositoryminer.persistence.handler;

import org.repositoryminer.persistence.Connection;

public class IndirectCodeMetricsDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "rm_indirect_code_metrics";
	
	public IndirectCodeMetricsDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}
	
}