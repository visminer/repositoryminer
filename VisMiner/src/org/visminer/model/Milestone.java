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

	@EmbeddedId
	private MilestonePK id;

	@Column(name="closed_issues")
	private int closedIssues;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	@Column(name="creator", nullable=false, length=45)
	private String creator;

	@Column(nullable=true, name="description", length=1000)
	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="due_date")
	private Date dueDate;

	@Column(name="opened_issues", nullable=true)
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
	private Repository repository;

	public Milestone() {
	}

	public MilestonePK getId() {
		return this.id;
	}

	public void setId(MilestonePK id) {
		this.id = id;
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