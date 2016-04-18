package org.repositoryminer.persistence.handler;

import org.repositoryminer.persistence.Connection;

public class RepositoryDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "repositories";

	public RepositoryDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}

}