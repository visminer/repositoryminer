package br.edu.ufba.softvis.visminer.model.database;

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
@NamedQuery(name="MilestoneDB.findAll", query="SELECT m FROM MilestoneDB m")
public class MilestoneDB implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MILESTONE_ID_GENERATOR", sequenceName="MILESTONE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MILESTONE_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="closed_issues", nullable=false)
	private int closedIssues;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date", nullable=false)
	private Date createDate;

	@Column(nullable=false, length=100)
	private String creator;

	@Lob
	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="due_date")
	private Date dueDate;

	@Column(nullable=false)
	private int number;

	@Column(name="opened_issues", nullable=false)
	private int openedIssues;

	@Column(nullable=false, length=6)
	private String state;

	@Column(nullable=false, length=500)
	private String title;

	//bi-directional many-to-one association to IssueDB
	@OneToMany(mappedBy="milestone")
	private List<IssueDB> issues;

	//bi-directional many-to-one association to RepositoryDB
	@ManyToOne
	@JoinColumn(name="repository_id", nullable=false)
	private RepositoryDB repository;

	public MilestoneDB() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
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

	public List<IssueDB> getIssues() {
		return this.issues;
	}

	public void setIssues(List<IssueDB> issues) {
		this.issues = issues;
	}

	public IssueDB addIssue(IssueDB issue) {
		getIssues().add(issue);
		issue.setMilestone(this);

		return issue;
	}

	public IssueDB removeIssue(IssueDB issue) {
		getIssues().remove(issue);
		issue.setMilestone(null);

		return issue;
	}

	public RepositoryDB getRepository() {
		return this.repository;
	}

	public void setRepository(RepositoryDB repository) {
		this.repository = repository;
	}

}