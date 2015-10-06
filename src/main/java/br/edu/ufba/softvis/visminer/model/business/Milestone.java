package br.edu.ufba.softvis.visminer.model.business;

import java.util.Date;
import java.util.List;

import br.edu.ufba.softvis.visminer.constant.StatusType;

public class Milestone {

	private int id;
	private int closedIssues;
	private Date createDate;
	private String creator;
	private String description;
	private Date dueDate;
	private int number;
	private int openedIssues;
	private StatusType status;
	private String title;
	private List<Issue> issues;
	
	public Milestone(){}
	
	/**
	 * @param id
	 * @param closedIssues
	 * @param createDate
	 * @param creator
	 * @param description
	 * @param dueDate
	 * @param number
	 * @param openedIssues
	 * @param status
	 * @param title
	 */
	public Milestone(int id, int closedIssues, Date createDate, String creator, String description, Date dueDate,
			int number, int openedIssues, StatusType status, String title) {
		super();
		this.id = id;
		this.closedIssues = closedIssues;
		this.createDate = createDate;
		this.creator = creator;
		this.description = description;
		this.dueDate = dueDate;
		this.number = number;
		this.openedIssues = openedIssues;
		this.status = status;
		this.title = title;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
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
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
	 * @return the dueDate
	 */
	public Date getDueDate() {
		return dueDate;
	}
	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
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
	 * @return the issues
	 */
	public List<Issue> getIssues() {
		return issues;
	}
	/**
	 * @param issues the issues to set
	 */
	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Milestone other = (Milestone) obj;
		if (id != other.id)
			return false;
		return true;
	}

}