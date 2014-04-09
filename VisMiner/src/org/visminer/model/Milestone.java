package org.visminer.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the milestone database table.
 * 
 */
@Entity
@Table(name="milestone")
@NamedQuery(name="Milestone.findAll", query="SELECT m FROM Milestone m")
public class Milestone implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idmilestone", unique=true, nullable=false)
	private int idmilestone;

	@Column(name="closed_issues", nullable=false)
	private int closedIssues;

	@Temporal(TemporalType.DATE)
	@Column(name="create_date", nullable=false)
	private Date createDate;

	@Column(name="creator", nullable=false, length=100)
	private String creator;

	@Column(name="description", length=1000)
	private String description;

	@Temporal(TemporalType.DATE)
	@Column(name="due_date")
	private Date dueDate;

	@Column(name="number", nullable=false)
	private int number;

	@Column(name="opened_issues", nullable=false)
	private int openedIssues;

	@Column(name="state", nullable=false, length=6)
	private String state;

	@Column(name="title", nullable=false, length=100)
	private String title;

	//bi-directional many-to-one association to Issue
	@OneToMany(mappedBy="milestone")
	private List<Issue> issues;

	//bi-directional many-to-one association to Repository
	@ManyToOne
	@JoinColumn(name="repository_idrepository", nullable=false)
	private Repository repository;

	public Milestone() {
	}

	public int getIdmilestone() {
		return this.idmilestone;
	}

	public void setIdmilestone(int idmilestone) {
		this.idmilestone = idmilestone;
	}

	public int getClosedIssues() {
		return this.closedIssues;
	}

	public void setClosedIssues(int closedIssues) {
		this.closedIssues = closedIssues;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
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

	public Repository getRepository() {
		return this.repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

}