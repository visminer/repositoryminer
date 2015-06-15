package br.edu.ufba.softvis.visminer.model.bean;

import java.util.Date;

public class Milestone {

	private int id;
	private int closedIssues;
	private Date createDate;
	private String creator;
	private String description;
	private Date dueDate;
	private int number;
	private int openedIssues;
	private String state;
	private String title;
	
	public Milestone(){}
	
	public Milestone(int id, int closedIssues, Date createDate, String creator,
			String description, Date dueDate, int number, int openedIssues,
			String state, String title) {
		super();
		this.id = id;
		this.closedIssues = closedIssues;
		this.createDate = createDate;
		this.creator = creator;
		this.description = description;
		this.dueDate = dueDate;
		this.number = number;
		this.openedIssues = openedIssues;
		this.state = state;
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getClosedIssues() {
		return closedIssues;
	}

	public void setClosedIssues(int closedIssues) {
		this.closedIssues = closedIssues;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getOpenedIssues() {
		return openedIssues;
	}

	public void setOpenedIssues(int openedIssues) {
		this.openedIssues = openedIssues;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}	
	
}
