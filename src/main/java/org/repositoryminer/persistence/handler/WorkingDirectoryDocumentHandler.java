package org.repositoryminer.persistence.handler;

import org.repositoryminer.persistence.Connection;

public class WorkingDirectoryDocumentHandler extends DocumentHandler{

	private static final String COLLECTION_NAME = "working_directories";

	public WorkingDirectoryDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}
	
}