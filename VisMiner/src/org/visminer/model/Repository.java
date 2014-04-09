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
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idrepository", unique=true, nullable=false)
	private int idrepository;

	@Column(name="name", nullable=false, length=45)
	private String name;

	@Column(name="path", nullable=false, length=1024)
	private String path;

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

	public int getIdrepository() {
		return this.idrepository;
	}

	public void setIdrepository(int idrepository) {
		this.idrepository = idrepository;
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

	public List<Version> getVersions() {
		return this.versions;
	}

	public void setVersions(List<Version> versions) {
		this.versions = versions;
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