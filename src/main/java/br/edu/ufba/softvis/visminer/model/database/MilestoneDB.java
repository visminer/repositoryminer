package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * The persistent class for the milestone database table.
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

	/**
	 * @param id
	 * @param closedIssues
	 * @param createDate
	 * @param creator
	 * @param description
	 * @param dueDate
	 * @param number
	 * @param openedIssues
	 * @param state
	 * @param title
	 */
	public MilestoneDB(int id, int closedIssues, Date createDate,
			String creator, String description, Date dueDate, int number,
			int openedIssues, String state, String title) {
		super();
		this.id = id;
		this.closedIssues = closedIssues;
		this.createDate = createDate;
		this.creator = creator;
		this.description = description;
		this.dueDate = dueDate;
		this.number = number;
		this.openedIssues = openedIssues;
		this.state = state;
		this.title = title;
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
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
	 * @return the dueDate
	 */
	public Date getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
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
	public List<IssueDB> getIssues() {
		return issues;
	}

	/**
	 * @param issues the issues to set
	 */
	public void setIssues(List<IssueDB> issues) {
		this.issues = issues;
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

}