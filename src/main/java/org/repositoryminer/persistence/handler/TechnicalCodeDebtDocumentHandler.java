package org.repositoryminer.persistence.handler;

import org.repositoryminer.persistence.Connection;

public class TechnicalCodeDebtDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "rm_technical_code_debt";

	public TechnicalCodeDebtDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}
	
}