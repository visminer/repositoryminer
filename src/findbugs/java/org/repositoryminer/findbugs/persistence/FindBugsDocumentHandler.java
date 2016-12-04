package org.repositoryminer.findbugs.persistence;

import org.repositoryminer.persistence.Connection;
import org.repositoryminer.persistence.handler.DocumentHandler;

public class FindBugsDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "findbugs_bugs_analysis";
	
	public FindBugsDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}
	
}