package org.repositoryminer.remoteminer.persistence;

import org.repositoryminer.persistence.Connection;
import org.repositoryminer.persistence.handler.DocumentHandler;

public class DefectMetricsDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "defect_metrics";

	public DefectMetricsDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}

}
