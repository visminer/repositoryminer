package org.visminer.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the repository database table.
 * 
 */
@Entity
@Table(name="repository")
@NamedQuery(name="Repository.findAll", query="SELECT r FROM Repository r")
public class Repository implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_git")
	private String idGit;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="creation_date")
	private Date creationDate;

	@Column(name="path", nullable=false, length=1024)
	private String path;

	//bi-directional many-to-one association to Branch
	@OneToMany(mappedBy="repository")
	private List<Branch> branches;

	//bi-directional many-to-one association to Committer
	@ManyToMany(mappedBy="repositories")
	private List<Committer> committers;

	//bi-directional many-to-one association to Issue
	@OneToMany(mappedBy="repository", cascade = CascadeType.ALL)
	private List<Issue> issues;

	//bi-directional many-to-one association to Milestone
	
	@OneToMany(mappedBy="repository", cascade = CascadeType.ALL)
	private List<Milestone> milestones;

	//bi-directional many-to-one association to Tag
	@OneToMany(mappedBy="repository")
	private List<Tag> tags; 

	public Repository() {
	}

	public String getIdGit() {
		return this.idGit;
	}

	public void setIdGit(String idGit) {
		this.idGit = idGit;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<Branch> getBranches() {
		return this.branches;
	}

	public void setBranches(List<Branch> branches) {
		this.branches = branches;
	}

	public Branch addBranch(Branch branch) {
		getBranches().add(branch);
		branch.setRepository(this);

		return branch;
	}

	public Branch removeBranch(Branch branch) {
		getBranches().remove(branch);
		branch.setRepository(null);

		return branch;
	}

	public List<Committer> getCommitters() {
		return this.committers;
	}

	public void setCommitters(List<Committer> committers) {
		this.committers = committers;
	}
/*
	public Committer addCommitter(Committer committer) {
		getCommitters().add(committer);
		committer.setRepository(this);

		return committer;
	}

	public Committer removeCommitter(Committer committer) {
		getCommitters().remove(committer);
		committer.setRepository(null);

		return committer;
	}*/

	public List<Issue> getIssues() {
		return this.issues;
	}

	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}

	public Issue addIssue(Issue issue) {
		getIssues().add(issue);
		issue.setRepository(this);

		return issue;
	}

	public Issue removeIssue(Issue issue) {
		getIssues().remove(issue);
		issue.setRepository(null);

		return issue;
	}

	public List<Milestone> getMilestones() {
		return this.milestones;
	}

	public void setMilestones(List<Milestone> milestones) {
		this.milestones = milestones;
	}

	public Milestone addMilestone(Milestone milestone) {
		getMilestones().add(milestone);
		milestone.setRepository(this);

		return milestone;
	}

	public Milestone removeMilestone(Milestone milestone) {
		getMilestones().remove(milestone);
		milestone.setRepository(null);

		return milestone;
	}

	public List<Tag> getTags() {
		return this.tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public Tag addTag(Tag tag) {
		getTags().add(tag);
		tag.setRepository(this);

		return tag;
	}

	public Tag removeTag(Tag tag) {
		getTags().remove(tag);
		tag.setRepository(null);

		return tag;
	}

}