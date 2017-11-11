package org.repositoryminer.web.scm.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

public class Comment {

	private String user;
	private String body;
	private Date createdAt;
	private Date updatedAt;

	public static List<Document> toDocumentList(List<Comment> comments) {
		List<Document> docs = new ArrayList<Document>();
		if (comments != null) {
			for (Comment c : comments) {
				docs.add(new Document("user", c.getUser()).append("body", c.getBody())
						.append("created_at", c.getCreatedAt()).append("updated_at", c.getUpdatedAt()));
			}
		}
		return docs;
	}

	public Comment() {
	}

	public Comment(String user, String body, Date createdAt, Date updatedAt) {
		super();
		this.user = user;
		this.body = body;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

}