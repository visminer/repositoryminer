package org.repositoryminer.persistence.handler;

import org.repositoryminer.persistence.Connection;

public class TechnicalDebtDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "rm_technical_debt";
	
	public TechnicalDebtDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}
	
}
