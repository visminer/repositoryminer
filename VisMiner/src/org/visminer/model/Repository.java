package org.visminer.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * <p>
 * The persistent class for the repository database table.
 * </p>
 * 
 * @author Felipe
 * @version 1.0
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
	
	/**
	 * @return the idGit
	 */
	public String getIdGit() {
		return idGit;
	}

	/**
	 * @param idGit the idGit to set
	 */
	public void setIdGit(String idGit) {
		this.idGit = idGit;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the createdAt
	 */
	public long getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the committers
	 */
	public List<Committer> getCommitters() {
		return committers;
	}

	/**
	 * @param committers the committers to set
	 */
	public void setCommitters(List<Committer> committers) {
		this.committers = committers;
	}

	/**
	 * @return the issues
	 */
	public List<Issue> getIssues() {
		return issues;
	}

	/**
	 * @param issues the issues to set
	 */
	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}

	/**
	 * @return the milestones
	 */
	public List<Milestone> getMilestones() {
		return milestones;
	}

	/**
	 * @param milestones the milestones to set
	 */
	public void setMilestones(List<Milestone> milestones) {
		this.milestones = milestones;
	}

	/**
	 * @return the versions
	 */
	public List<Version> getVersions() {
		return versions;
	}

	/**
	 * @param versions the versions to set
	 */
	public void setVersions(List<Version> versions) {
		this.versions = versions;
	}

	/**
	 * 
	 * @param committer
	 * @return committer added
	 */
	public Committer addCommitter(Committer committer) {
		getCommitters().add(committer);
		committer.setRepository(this);

		return committer;
	}

	/**
	 * 
	 * @param committer
	 * @return committer removed
	 */
	public Committer removeCommitter(Committer committer) {
		getCommitters().remove(committer);
		committer.setRepository(null);

		return committer;
	}

	/**
	 * 
	 * @param issue
	 * @return issue added
	 */
	public Issue addIssue(Issue issue) {
		getIssues().add(issue);
		issue.setRepository(this);

		return issue;
	}

	/**
	 * 
	 * @param issue
	 * @return issue removed
	 */
	public Issue removeIssue(Issue issue) {
		getIssues().remove(issue);
		issue.setRepository(null);

		return issue;
	}

	/**
	 * 
	 * @param milestone
	 * @return milestone added
	 */
	public Milestone addMilestone(Milestone milestone) {
		getMilestones().add(milestone);
		milestone.setRepository(this);

		return milestone;
	}

	/**
	 * 
	 * @param milestone
	 * @return milestone removed
	 */
	public Milestone removeMilestone(Milestone milestone) {
		getMilestones().remove(milestone);
		milestone.setRepository(null);

		return milestone;
	}

	/**
	 * 
	 * @param version
	 * @return version added
	 */
	public Version addVersion(Version version) {
		getVersions().add(version);
		version.setRepository(this);

		return version;
	}

	/**
	 * 
	 * @param version
	 * @return version removed
	 */
	public Version removeVersion(Version version) {
		getVersions().remove(version);
		version.setRepository(null);

		return version;
	}

}