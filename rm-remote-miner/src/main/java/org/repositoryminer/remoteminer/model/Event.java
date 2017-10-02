package org.repositoryminer.remoteminer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

public class Event {

	private String description;
	private String creator;
	private Date createdAt;
	private String commitId;

	public static List<Document> toDocumentList(List<Event> events) {
		List<Document> docs = new ArrayList<Document>();
		if (events != null) {
			for (Event e : events) {
				docs.add(new Document("description", e.getDescription()).append("creator", e.getCreator())
						.append("created_at", e.getCreatedAt()).append("commit", e.getCommitId()));
			}
		}
		return docs;
	}

	public Event() {
	}

	public Event(String description, String creator, Date createdAt, String commitId) {
		super();
		this.description = description;
		this.creator = creator;
		this.createdAt = createdAt;
		this.commitId = commitId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

}
