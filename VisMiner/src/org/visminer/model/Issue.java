package org.visminer.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
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
	@ManyToOne(optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "repository_idGit", referencedColumnName = "idGit")
	private Repository repository;

	@Column(name="assignee", length=39)
	private String assignee;

	@Column(name = "closed_date")
	private long closed_date;
	
	@Column(name = "create_date", nullable = false)
	private long create_date;
	
	@Column(name = "updated_date")
	private long updated_date;

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
	@ManyToOne(optional = true, cascade = CascadeType.ALL)
	@JoinColumns({
        @JoinColumn(name = "milestone_number", referencedColumnName = "number"),
        @JoinColumn(name = "milestone_repository_idGit", referencedColumnName = "repository_idGit")
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

	public long getClosed_date() {
		return closed_date;
	}

	public void setClosed_date(long closed_date) {
		this.closed_date = closed_date;
	}

	public long getCreate_date() {
		return create_date;
	}

	public void setCreate_date(long create_date) {
		this.create_date = create_date;
	}

	public long getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(long updated_date) {
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

class IssuePK implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int number;
	private String repository;
	
	public IssuePK(){
		
	}


}
