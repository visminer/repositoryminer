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

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idissue", unique=true, nullable=false)
	private int idissue;

	@Column(name="assignee", nullable=false, length=100)
	private String assignee;

	@Temporal(TemporalType.DATE)
	@Column(name="closed_date")
	private Date closedDate;

	@Temporal(TemporalType.DATE)
	@Column(name="create_date", nullable=false)
	private Date createDate;

	@Lob
	@Column(name="labels")
	private byte[] labels;

	@Column(name="number", nullable=false)
	private int number;

	@Column(name="status", length=6)
	private String status;

	@Column(name="title", nullable=false, length=100)
	private String title;

	@Temporal(TemporalType.DATE)
	@Column(name="update_date")
	private Date updateDate;

	//bi-directional many-to-many association to Commit
	@ManyToMany(mappedBy="issues")
	private List<Commit> commits;

	//bi-directional many-to-one association to Milestone
	@ManyToOne
	@JoinColumn(name="milestone_idmilestone")
	private Milestone milestone;

	//bi-directional many-to-one association to Repository
	@ManyToOne
	@JoinColumn(name="repository_idrepository")
	private Repository repository;

	public Issue() {
	}

	public int getIdissue() {
		return this.idissue;
	}

	public void setIdissue(int idissue) {
		this.idissue = idissue;
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

	public byte[] getLabels() {
		return this.labels;
	}

	public void setLabels(byte[] labels) {
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