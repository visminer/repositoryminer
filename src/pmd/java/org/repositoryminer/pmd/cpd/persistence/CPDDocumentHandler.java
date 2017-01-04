package org.repositoryminer.pmd.cpd.persistence;

import org.repositoryminer.persistence.Connection;
import org.repositoryminer.persistence.handler.DocumentHandler;

public class CPDDocumentHandler extends DocumentHandler{

	private static final String COLLECTION_NAME = "pmd_cpd_analysis";
	
	public CPDDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}
	
}