package org.visminer.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the issue database table.
 * 
 */
@Entity
@Table(name="issue")
@IdClass(IssuePK.class)
@NamedQuery(name="Issue.findAll", query="SELECT i FROM Issue i")
public class Issue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "number")
	private int number;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "repository_id_git", referencedColumnName = "id_git")
	private Repository repository;

	@Column(name="assignee", length=39)
	private String assignee;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "closed_date")
	private Date closed_date;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", nullable = false)
	private Date create_date;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date")
	private Date updated_date;

	@Column(name="labels")
	private  ArrayList<String> labels = new ArrayList<String>();

	@Column(name="status", nullable = false, length=6)
	private String status;

	@Column(name="title", nullable=false, length=500)
	private String title;

	//bi-directional many-to-many association to Commit
	@ManyToMany(mappedBy="issues")
	private List<Commit> commits;

	//bi-directional many-to-one association to Milestone
	@ManyToOne(optional = true, cascade = CascadeType.REFRESH)
	@JoinColumns({
        @JoinColumn(name = "milestone_number", referencedColumnName = "number"),
        @JoinColumn(name = "milestone_repository_id_git", referencedColumnName = "repository_id_git")
    })
	private Milestone milestone;
		
	@Column(name = "numberOfComments", nullable = false)
	private int numberOfComments;

	public Issue() {
	}

	//getters and setters
	public String getAssignee() {
		return this.assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public Date getClosed_date() {
		return closed_date;
	}

	public void setClosed_date(Date closed_date) {
		this.closed_date = closed_date;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}

	public int getNumberOfComments() {
		return numberOfComments;
	}

	public void setNumberOfComments(int numberOfComments) {
		this.numberOfComments = numberOfComments;
	}

	public ArrayList<String> getLabels() {
		return labels;
	}


	public void setLabels(ArrayList<String> labels) {
		this.labels = labels;
	}


	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Commit> getCommits() {
		return this.commits;
	}

	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}

	public Milestone getMilestone() {
		return this.milestone;
	}

	public void setMilestone(Milestone milestone) {
		this.milestone = milestone;
	}

	public Repository getRepository() {
		return this.repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

}
