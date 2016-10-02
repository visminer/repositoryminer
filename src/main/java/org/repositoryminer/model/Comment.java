package org.repositoryminer.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class Comment {

	private String user;
	private String body;

	public static List<Document> toDocumentList(List<Comment> comments) {
		List<Document> docs = new ArrayList<Document>();
		
		if (comments != null) {
			for (Comment c : comments) {
				docs.add(new Document("user", c.getUser()).append("body", c.getBody()));
			}
		}
		
		return docs;
	}

	public Comment() {
	}

	public Comment(String user, String body) {
		this.user = user;
		this.body = body;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
