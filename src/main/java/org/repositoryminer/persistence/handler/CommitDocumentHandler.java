package org.repositoryminer.persistence.handler;

import org.repositoryminer.persistence.Connection;

public class CommitDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "rm_commits";

	public CommitDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}

}