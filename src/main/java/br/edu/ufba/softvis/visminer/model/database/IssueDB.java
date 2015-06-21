package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * The persistent class for the issue database table.
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
	public IssueDB(int id, String assignee, Date closedDate,
			int commentsNumber, String createDate, String labels, int number,
			String status, String title, Date updateDate) {
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

	/**
	 * @return the milestone
	 */
	public MilestoneDB getMilestone() {
		return milestone;
	}

	/**
	 * @param milestone the milestone to set
	 */
	public void setMilestone(MilestoneDB milestone) {
		this.milestone = milestone;
	}

	/**
	 * @return the repository
	 */
	public RepositoryDB getRepository() {
		return repository;
	}

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(RepositoryDB repository) {
		this.repository = repository;
	}

	/**
	 * @return the commits
	 */
	public List<CommitDB> getCommits() {
		return commits;
	}

	/**
	 * @param commits the commits to set
	 */
	public void setCommits(List<CommitDB> commits) {
		this.commits = commits;
	}

}