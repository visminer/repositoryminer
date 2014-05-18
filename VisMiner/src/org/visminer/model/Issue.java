package org.visminer.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the issue database table.
 * 
 */
@Entity
@Table(name="issue")
@NamedQuery(name="Issue.findAll", query="SELECT i FROM Issue i")
public class Issue implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private IssuePK id;

	@Column(name="assignee", length=100, nullable=true)
	private String assignee;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="closed_date")
	private Date closedDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	private String milestone_repository_idGit;

	@Column(name="comments_number", nullable=false)
	private int commentsNumber;

	@Column(name="status", length=6, nullable=false)
	private String status;

	@Column(name="title", length=500, nullable=false)
	private String title;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_date")
	private Date updatedDate;

	//bi-directional many-to-many association to Commit
	@ManyToMany
	@JoinTable(
		name="commit_has_issue"
		, joinColumns={
			@JoinColumn(name="issue_number", referencedColumnName="number"),
			@JoinColumn(name="issue_repository_id_git", referencedColumnName="repository_id_git")
			}
		, inverseJoinColumns={
			@JoinColumn(name="commit_sha")
			}
		)
	private List<Commit> commits;

	//bi-directional many-to-one association to Repository
	@ManyToOne
	private Repository repository;

	//bi-directional many-to-one association to Milestone
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="milestone_number", referencedColumnName="number"),
		@JoinColumn(name="milestone_repository_id_git", referencedColumnName="repository_id_git")
		})
	private Milestone milestone;

	//bi-directional many-to-one association to Label
	@OneToMany(mappedBy="issue")
	private List<Label> labels;

	public Issue() {
	}

	public IssuePK getId() {
		return this.id;
	}

	public void setId(IssuePK id) {
		this.id = id;
	}

	public String getAssignee() {
		return this.assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public Date getClosedDate() {
		return this.closedDate;
	}

	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getMilestone_repository_idGit() {
		return this.milestone_repository_idGit;
	}

	public void setMilestone_repository_idGit(String milestone_repository_idGit) {
		this.milestone_repository_idGit = milestone_repository_idGit;
	}

	public int getCommentsNumber() {
		return this.commentsNumber;
	}

	public void setCommentsNumber(int commentsNumber) {
		this.commentsNumber = commentsNumber;
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

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public List<Commit> getCommits() {
		return this.commits;
	}

	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}

	public Repository getRepository() {
		return this.repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public Milestone getMilestone() {
		return this.milestone;
	}

	public void setMilestone(Milestone milestone) {
		this.milestone = milestone;
	}

	public List<Label> getLabels() {
		return this.labels;
	}

	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}

	public Label addLabel(Label label) {
		getLabels().add(label);
		label.setIssue(this);

		return label;
	}

	public Label removeLabel(Label label) {
		getLabels().remove(label);
		label.setIssue(null);

		return label;
	}

}