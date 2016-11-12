package org.repositoryminer.findbugs.persistence;

import org.repositoryminer.persistence.Connection;
import org.repositoryminer.persistence.handler.DocumentHandler;

public class FindBugsPersistence extends DocumentHandler {

	private static final String COLLECTION_NAME = "rm_findbugs_analysis";
	
	public FindBugsPersistence() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}
	
}