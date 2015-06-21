package br.edu.ufba.softvis.visminer.model.bean;

import java.util.Date;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * 
 * Simple issue bean.
 * This bean is used to simplify interaction between some parts, avoiding coupling and doing smaller core codes.
 */

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

	/**
	 * @param id
	 * @param assignee
	 * @param closedDate
	 * @param commentsNumber
	 * @param createDate
	 * @param labels
	 * @param number
	 * @param status
	 * @param title
	 * @param updateDate
	 */
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
	 * @return the closedDate
	 */
	public Date getClosedDate() {
		return closedDate;
	}

	/**
	 * @param closedDate the closedDate to set
	 */
	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}

	/**
	 * @return the commentsNumber
	 */
	public int getCommentsNumber() {
		return commentsNumber;
	}

	/**
	 * @param commentsNumber the commentsNumber to set
	 */
	public void setCommentsNumber(int commentsNumber) {
		this.commentsNumber = commentsNumber;
	}

	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the labels
	 */
	public String getLabels() {
		return labels;
	}

	/**
	 * @param labels the labels to set
	 */
	public void setLabels(String labels) {
		this.labels = labels;
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
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
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
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
}