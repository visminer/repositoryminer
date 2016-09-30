package org.repositoryminer.persistence.handler;

import org.repositoryminer.persistence.Connection;

public class SnapshotAnalysisDocumentHandler extends DocumentHandler{

	private static final String COLLECTION_NAME = "rm_snapshot_analysis";
	
	public SnapshotAnalysisDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}
	
}