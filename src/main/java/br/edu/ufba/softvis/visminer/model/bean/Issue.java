package br.edu.ufba.softvis.visminer.model.bean;

import java.util.Date;

public class Issue {

	private int id;
	private String assignee;
	private Date closedDate;
	private int commentsNumber;
	private String createDate;
	private String labels;
	private int number;
	private String status;
	private String title;
	private Date updateDate;
	
	public Issue(){}
	
	public Issue(int id, String assignee, Date closedDate, int commentsNumber,
			String createDate, String labels, int number, String status,
			String title, Date updateDate) {
		super();
		this.id = id;
		this.assignee = assignee;
		this.closedDate = closedDate;
		this.commentsNumber = commentsNumber;
		this.createDate = createDate;
		this.labels = labels;
		this.number = number;
		this.status = status;
		this.title = title;
		this.updateDate = updateDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public Date getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}

	public int getCommentsNumber() {
		return commentsNumber;
	}

	public void setCommentsNumber(int commentsNumber) {
		this.commentsNumber = commentsNumber;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}	
	
}
