package org.repositoryminer.persistence.model;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.scm.hostingservice.StatusType;

public class MilestoneDB {

	private int number;
	private StatusType status;
	private String title;
	private String description;
	private String creator;
	private int openedIssues;
	private int closedIssues;
	private Date createdAt;
	private Date dueOn;
	private List<Integer> issues;
	private String repository;
	
	public MilestoneDB() {}
	
	public MilestoneDB(int number, StatusType status, String title, String description, int openedIssues,
			int closedIssues, Date createdAt, Date dueOn) {
		super();
		this.number = number;
		this.status = status;
		this.title = title;
		this.description = description;
		this.openedIssues = openedIssues;
		this.closedIssues = closedIssues;
		this.createdAt = createdAt;
		this.dueOn = dueOn;
	}

	public Document toDocument() {
		Document doc = new Document();
		doc.append("number", number).append("status", status.toString()).append("title", title)
				.append("description", description).append("creator", creator).append("opened_issues", openedIssues)
				.append("closed_issues", closedIssues).append("created_at", createdAt).append("due_on", dueOn)
				.append("repository", repository).append("issues", issues);
		return doc;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @return the openedIssues
	 */
	public int getOpenedIssues() {
		return openedIssues;
	}
	/**
	 * @param openedIssues the openedIssues to set
	 */
	public void setOpenedIssues(int openedIssues) {
		this.openedIssues = openedIssues;
	}
	/**
	 * @return the closedIssues
	 */
	public int getClosedIssues() {
		return closedIssues;
	}
	/**
	 * @param closedIssues the closedIssues to set
	 */
	public void setClosedIssues(int closedIssues) {
		this.closedIssues = closedIssues;
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
	 * @return the dueOn
	 */
	public Date getDueOn() {
		return dueOn;
	}
	/**
	 * @param dueOn the dueOn to set
	 */
	public void setDueOn(Date dueOn) {
		this.dueOn = dueOn;
	}
	/**
	 * @return the issues
	 */
	public List<Integer> getIssues() {
		return issues;
	}
	/**
	 * @param issues the issues to set
	 */
	public void setIssues(List<Integer> issues) {
		this.issues = issues;
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

}