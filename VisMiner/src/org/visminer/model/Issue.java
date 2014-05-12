package org.visminer.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * The persistent class for the issue database table.
 * </p>
 * 
 * @author Felipe
 * @version 1.0
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
	@ManyToOne(cascade = CascadeType.ALL)
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
	 * @return the repository
	 */
	public Repository getRepository() {
		return repository;
	}

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(Repository repository) {
		this.repository = repository;
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
	 * @return the closed_date
	 */
	public long getClosed_date() {
		return closed_date;
	}

	/**
	 * @param closed_date the closed_date to set
	 */
	public void setClosed_date(long closed_date) {
		this.closed_date = closed_date;
	}

	/**
	 * @return the create_date
	 */
	public long getCreate_date() {
		return create_date;
	}

	/**
	 * @param create_date the create_date to set
	 */
	public void setCreate_date(long create_date) {
		this.create_date = create_date;
	}

	/**
	 * @return the updated_date
	 */
	public long getUpdated_date() {
		return updated_date;
	}

	/**
	 * @param updated_date the updated_date to set
	 */
	public void setUpdated_date(long updated_date) {
		this.updated_date = updated_date;
	}

	/**
	 * @return the labels
	 */
	public ArrayList<String> getLabels() {
		return labels;
	}

	/**
	 * @param labels the labels to set
	 */
	public void setLabels(ArrayList<String> labels) {
		this.labels = labels;
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
	 * @return the commits
	 */
	public List<Commit> getCommits() {
		return commits;
	}

	/**
	 * @param commits the commits to set
	 */
	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}

	/**
	 * @return the milestone
	 */
	public Milestone getMilestone() {
		return milestone;
	}

	/**
	 * @param milestone the milestone to set
	 */
	public void setMilestone(Milestone milestone) {
		this.milestone = milestone;
	}

	/**
	 * @return the numberOfComments
	 */
	public int getNumberOfComments() {
		return numberOfComments;
	}

	/**
	 * @param numberOfComments the numberOfComments to set
	 */
	public void setNumberOfComments(int numberOfComments) {
		this.numberOfComments = numberOfComments;
	}
	
}

class IssuePK implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int number;
	private String repository;
	
	public IssuePK(){
		
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
	 * @return the repository
	 */
	public String getRepository() {
		return repository;
	}

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(String repository) {
		this.repository = repository;
	}
	
	

}
