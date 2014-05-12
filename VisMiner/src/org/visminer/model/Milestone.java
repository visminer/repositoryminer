package org.visminer.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * <p>
 * The persistent class for the milestone database table.
 * </p>
 * 
 * @author Felipe
 * @version 1.0
 */
@Entity
@Table(name="milestone")
@IdClass(MilestonePK.class)
@NamedQuery(name="Milestone.findAll", query="SELECT m FROM Milestone m")
public class Milestone implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id 
	@Column(name = "number")
	private int number;
	
	@Id
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "repository_idGit", referencedColumnName = "idGit")
	private Repository repository;

	@Column(name="closed_issues", nullable=false)
	private int closedIssues;

	@Column(name = "create_date", nullable = false)
	private long create_date;

	@Column(name="creator", nullable=false, length=100)
	private String creator;

	@Column(name="description", length=1000)
	private String description;

	@Column(name = "due_date")
	private long due_date;

	@Column(name="opened_issues", nullable=false)
	private int openedIssues;

	@Column(name="state", nullable=false, length=6)
	private String state;

	@Column(name="title", nullable=false, length=500)
	private String title;

	//bi-directional many-to-one association to Issue
	@OneToMany(mappedBy="milestone")
	private List<Issue> issues;

	
	public Milestone() {
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
	 * @return the repository
	 */
	public Repository getRepository() {
		return repository;
	}

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(Repository repository) {
		this.repository = repository;
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
	 * @return the create_date
	 */
	public long getCreate_date() {
		return create_date;
	}

	/**
	 * @param create_date the create_date to set
	 */
	public void setCreate_date(long create_date) {
		this.create_date = create_date;
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
	 * @return the due_date
	 */
	public long getDue_date() {
		return due_date;
	}

	/**
	 * @param due_date the due_date to set
	 */
	public void setDue_date(long due_date) {
		this.due_date = due_date;
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

	/**
	 * 
	 * @param issue
	 * @return issue added
	 */
	public Issue addIssue(Issue issue) {
		getIssues().add(issue);
		issue.setMilestone(this);

		return issue;
	}

	/**
	 * 
	 * @param issue
	 * @return issue removed
	 */
	public Issue removeIssue(Issue issue) {
		getIssues().remove(issue);
		issue.setMilestone(null);

		return issue;
	}

}

class MilestonePK implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int number;
	private String repository;
	
	public MilestonePK(){
		
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