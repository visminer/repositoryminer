package org.repositoryminer.excomment.persistence;

import org.repositoryminer.persistence.Connection;
import org.repositoryminer.persistence.handler.DocumentHandler;

public class ExCommentDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "excomment_comments_analysis";
	
	public ExCommentDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}
	
}