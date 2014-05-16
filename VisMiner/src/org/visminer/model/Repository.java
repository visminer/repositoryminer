package org.visminer.model;

import java.io.Serializable;

import javax.persistence.*;

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
	@Column(name="idGit")
	private String idGit;

	@Column(name="name", length=45, nullable=false)
	private String name;

	@Column(name="path", length=1024, nullable=false)
	private String path;
	
	@Column(name = "createdAt")
	private long createdAt;

	//bi-directional many-to-one association to Committer
	@OneToMany(mappedBy="repository", fetch=FetchType.LAZY)
	private List<Committer> committers;

	//bi-directional many-to-one association to Issue
	@OneToMany(mappedBy="repository", fetch=FetchType.LAZY)
	private List<Issue> issues;

	//bi-directional many-to-one association to Milestone
	@OneToMany(mappedBy="repository", fetch=FetchType.LAZY)
	private List<Milestone> milestones;

	//bi-directional many-to-one association to Version
	@OneToMany(mappedBy="repository", fetch=FetchType.LAZY)
	private List<Version> versions;

	
	public Repository() {
	}

	
	//getters and setters
	public String getIdGit() {
		return idGit;
	}

	public void setIdGit(String idGit) {
		this.idGit = idGit;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<Committer> getCommitters() {
		return this.committers;
	}

	public void setCommitters(List<Committer> committers) {
		this.committers = committers;
	}
	
	public List<Version> getVersions() {
		return this.versions;
	}

	public void setVersions(List<Version> versions) {
		this.versions = versions;
	}
	
	public List<Issue> getIssues() {
		return this.issues;
	}

	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}
	
	public List<Milestone> getMilestones() {
		return this.milestones;
	}

	public void setMilestones(List<Milestone> milestones) {
		this.milestones = milestones;
	}


	public Committer addCommitter(Committer committer) {
		getCommitters().add(committer);
		committer.setRepository(this);

		return committer;
	}

	public Committer removeCommitter(Committer committer) {
		getCommitters().remove(committer);
		committer.setRepository(null);

		return committer;
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

	public Version addVersion(Version version) {
		getVersions().add(version);
		version.setRepository(this);

		return version;
	}

	public Version removeVersion(Version version) {
		getVersions().remove(version);
		version.setRepository(null);

		return version;
	}

}