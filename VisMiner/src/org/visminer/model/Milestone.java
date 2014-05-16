package org.visminer.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the milestone database table.
 * 
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
	

	//getters and setters
	public int getClosedIssues() {
		return this.closedIssues;
	}

	public void setClosedIssues(int closedIssues) {
		this.closedIssues = closedIssues;
	}

	public long getCreate_date() {
		return create_date;
	}

	public void setCreate_date(long create_date) {
		this.create_date = create_date;
	}

	public long getDue_date() {
		return due_date;
	}

	public void setDue_date(long due_date) {
		this.due_date = due_date;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getOpenedIssues() {
		return this.openedIssues;
	}

	public void setOpenedIssues(int openedIssues) {
		this.openedIssues = openedIssues;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Issue> getIssues() {
		return this.issues;
	}

	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}
	
	public Repository getRepository() {
		return this.repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}
	
	public Issue addIssue(Issue issue) {
		getIssues().add(issue);
		issue.setMilestone(this);

		return issue;
	}

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


}