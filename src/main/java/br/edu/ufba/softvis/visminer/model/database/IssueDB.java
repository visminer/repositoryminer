package br.edu.ufba.softvis.visminer.model.database;

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
@NamedQuery(name="IssueDB.findAll", query="SELECT i FROM IssueDB i")
public class IssueDB implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ISSUE_ID_GENERATOR", sequenceName="ISSUE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ISSUE_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private int id;

	@Column(length=45)
	private String assignee;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="closed_date")
	private Date closedDate;

	@Column(name="comments_number", nullable=false)
	private int commentsNumber;

	@Column(name="create_date", nullable=false, length=45)
	private String createDate;

	@Lob
	private String labels;

	@Column(nullable=false)
	private int number;

	@Column(nullable=false, length=6)
	private String status;

	@Column(nullable=false, length=500)
	private String title;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date")
	private Date updateDate;

	//bi-directional many-to-one association to MilestoneDB
	@ManyToOne
	@JoinColumn(name="milestone_id")
	private MilestoneDB milestone;

	//bi-directional many-to-one association to RepositoryDB
	@ManyToOne
	@JoinColumn(name="repository_id", nullable=false)
	private RepositoryDB repository;

	//bi-directional many-to-many association to CommitDB
	@ManyToMany
	@JoinTable(
		name="issue_reference_commit"
		, joinColumns={
			@JoinColumn(name="issue_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="commit_id", nullable=false)
			}
		)
	private List<CommitDB> commits;

	public IssueDB() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
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

	public int getCommentsNumber() {
		return this.commentsNumber;
	}

	public void setCommentsNumber(int commentsNumber) {
		this.commentsNumber = commentsNumber;
	}

	public String getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLabels() {
		return this.labels;
	}

	public void setLabels(String labels) {
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

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public MilestoneDB getMilestone() {
		return this.milestone;
	}

	public void setMilestone(MilestoneDB milestone) {
		this.milestone = milestone;
	}

	public RepositoryDB getRepository() {
		return this.repository;
	}

	public void setRepository(RepositoryDB repository) {
		this.repository = repository;
	}

	public List<CommitDB> getCommits() {
		return this.commits;
	}

	public void setCommits(List<CommitDB> commits) {
		this.commits = commits;
	}

}