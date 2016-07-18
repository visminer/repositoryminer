package org.repositoryminer.persistence.model;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.scm.hostingservice.StatusType;

public class IssueDB {

	private String creator;
	private String assignee;
	private Date closedAt;
	private int comments;
	private Date createdAt;
	private int number;
	private StatusType status;
	private String title;
	private Date updatedAt;
	private String body; 
	private int milestone;
	private String repository;
	private List<LabelDB> labels;
	
	public IssueDB() {}

	public IssueDB(String creator, Date closedAt, int comments, Date createdAt, int number, StatusType status,
			String title, Date updatedAt, String body) {
		super();
		this.creator = creator;
		this.closedAt = closedAt;
		this.comments = comments;
		this.createdAt = createdAt;
		this.number = number;
		this.status = status;
		this.title = title;
		this.updatedAt = updatedAt;
		this.body = body;
	}

	public Document toDocument() {
		Document doc = new Document();
		doc.append("creator", creator).append("assignee", assignee).append("closed_at", closedAt)
				.append("comments", comments).append("created_at", createdAt).append("number", number)
				.append("status", status.toString()).append("title", title).append("updated_at", updatedAt)
				.append("body", body).append("milestone", milestone).append("repository", repository)
				.append("labels", LabelDB.toDocumentList(labels));
		return doc;
	}
	
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the assignee
	 */
	public String getAssignee() {
		return assignee;
	}

	/**
	 * @param assignee the assignee to set
	 */
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	/**
	 * @return the closedAt
	 */
	public Date getClosedAt() {
		return closedAt;
	}

	/**
	 * @param closedAt the closedAt to set
	 */
	public void setClosedAt(Date closedAt) {
		this.closedAt = closedAt;
	}

	/**
	 * @return the comments
	 */
	public int getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(int comments) {
		this.comments = comments;
	}

	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * @return the status
	 */
	public StatusType getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusType status) {
		this.status = status;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the updatedAt
	 */
	public Date getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the milestone
	 */
	public int getMilestone() {
		return milestone;
	}

	/**
	 * @param milestone the milestone to set
	 */
	public void setMilestone(int milestone) {
		this.milestone = milestone;
	}

	/**
	 * @return the repository
	 */
	public String getRepository() {
		return repository;
	}

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(String repository) {
		this.repository = repository;
	}

	/**
	 * @return the labels
	 */
	public List<LabelDB> getLabels() {
		return labels;
	}

	/**
	 * @param labels the labels to set
	 */
	public void setLabels(List<LabelDB> labels) {
		this.labels = labels;
	}
	
}