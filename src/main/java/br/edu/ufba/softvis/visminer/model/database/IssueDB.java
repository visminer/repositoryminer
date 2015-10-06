package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.edu.ufba.softvis.visminer.constant.StatusType;
import br.edu.ufba.softvis.visminer.model.business.Issue;


/**
 * The persistent class for the issue database table.
 */
@Entity
@Table(name="issue")
@NamedQueries({
	@NamedQuery(name="IssueDB.minFindByRepository", query="SELECT i.number, i.id FROM IssueDB i where i.repository.id = :repositoryId"),
	@NamedQuery(name="IssueDB.findByRepository", query="SELECT i FROM IssueDB i where i.repository.id = :repositoryId")
})
public class IssueDB implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ISSUE_ID_GENERATOR", sequenceName="ISSUE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ISSUE_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private int id;

	@Column(length=100, nullable=false)
	private String creator;
	
	@Column(length=100)
	private String assignee;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="closed_date")
	private Date closedDate;

	@Column(name="comments_number", nullable=false)
	private int commentsNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date", nullable=false)
	private Date createDate;

	@Column(nullable=false)
	private int number;

	@Column(nullable=false)
	private int status;

	@Column(nullable=false, length=500)
	private String title;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date")
	private Date updateDate;

	@Lob
	private String body; 
	
	//bi-directional many-to-one association to MilestoneDB
	@ManyToOne
	@JoinColumn(name="milestone_id")
	private MilestoneDB milestone;

	//bi-directional many-to-one association to RepositoryDB
	@ManyToOne
	@JoinColumn(name="repository_id", nullable=false)
	private RepositoryDB repository;

	@OneToMany(mappedBy="issue", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<LabelDB> labels;
	
	@OneToMany(mappedBy="issue")
	private List<CommitReferenceIssueDB> commitReferenceIssues;
	
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
	 * @param body
	 */
	public IssueDB(int id, String creator, String assignee, Date closedDate,
			int commentsNumber, Date createDate, int number,
			StatusType status, String title, Date updateDate, String body) {
		super();
		this.id = id;
		this.creator = creator;
		this.assignee = assignee;
		this.closedDate = closedDate;
		this.commentsNumber = commentsNumber;
		this.createDate = createDate;
		this.number = number;
		this.status = status != null ? status.getId() : 0;
		this.title = title;
		this.updateDate = updateDate;
		this.body = body;
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
	public StatusType getStatus() {
		return StatusType.parse(status);
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusType status) {
		this.status = status != null ? status.getId() : 0;
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
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
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
	 * @return the commitReferenceIssues
	 */
	public List<CommitReferenceIssueDB> getCommitReferenceIssues() {
		return commitReferenceIssues;
	}

	/**
	 * @param commitReferenceIssues the commitReferenceIssues to set
	 */
	public void setCommitReferenceIssues(List<CommitReferenceIssueDB> commitReferenceIssues) {
		this.commitReferenceIssues = commitReferenceIssues;
	}

	/**
	 * @return the labels
	 */
	public List<LabelDB> getLabels() {
		return labels;
	}

	/**
	 * @param labels the labels to set
	 */
	public void setLabels(List<LabelDB> labels) {
		this.labels = labels;
	}
	
	public static List<Issue> toBusiness(List<IssueDB> issues){
		
		List<Issue> bizzIssues = new ArrayList<Issue>();
		
		if(issues == null)
			return bizzIssues;
		
		for(IssueDB i : issues){
			
			Issue issue = new Issue(i.getId(), i.getCreator(), i.getAssignee(),
					i.getClosedDate(), i.getCommentsNumber(), i.getCreateDate(), i.getNumber(),
					i.getStatus(), i.getTitle(), i.getUpdateDate(), i.getBody());
			issue.setLabels(LabelDB.toBusiness(i.getLabels()));
			
			if(i.getMilestone() != null)
				issue.setMilestone(i.getMilestone().toBusiness());
			
			bizzIssues.add(issue);
			
		}
		
		return bizzIssues;
		
	}
	
	public Issue toBusiness(){
		return new Issue(id, creator, assignee, closedDate, commentsNumber, 
				createDate, commentsNumber, getStatus(), title, updateDate, body);
	}

}