package br.edu.ufba.softvis.visminer.model.bean;

import java.util.Date;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * 
 * Simple milestone bean.
 * This bean is used to simplify interaction between some parts, avoiding coupling and doing smaller core codes.
 */

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

	/**
	 * @param id
	 * @param closedIssues
	 * @param createDate
	 * @param creator
	 * @param description
	 * @param dueDate
	 * @param number
	 * @param openedIssues
	 * @param state
	 * @param title
	 */
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
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
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
	
}