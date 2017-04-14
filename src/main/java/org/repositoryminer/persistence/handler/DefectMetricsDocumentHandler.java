package org.repositoryminer.persistence.handler;

import org.repositoryminer.persistence.Connection;

public class DefectMetricsDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "defect_metrics";

	public DefectMetricsDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}

}
