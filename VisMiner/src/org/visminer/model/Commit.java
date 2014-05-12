package org.visminer.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * <p>
 * The persistent class for the commit database table.
 * </p>
 * @author Felipe
 * @version 1.0
 */
@Entity
@Table(name="commit")
@NamedQuery(name="Commit.findAll", query="SELECT c FROM Commit c")
public class Commit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="sha", unique=true, nullable=false, length=45)
	private String sha;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date", nullable=false)
	private Date date;

	@Column(name="message", nullable=false, length=10000)
	private String message;

	//bi-directional many-to-one association to Committer
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="committer_idcommitter", nullable=false)
	private Committer committer;

	//bi-directional many-to-many association to Issue
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="commit_reference_issue",
		joinColumns = {
			@JoinColumn(name="commit_sha", nullable=false, referencedColumnName = "sha")
		},
		inverseJoinColumns={
			@JoinColumn(name = "issue_number", referencedColumnName = "number"),
			@JoinColumn(name = "issue_repository_idGit", referencedColumnName = "repository_idGit")
		}
	)
	private List<Issue> issues;
	

	//bi-directional many-to-many association to Version
	@ManyToMany(mappedBy="commits", fetch=FetchType.LAZY)
	private List<Version> versions;

	//bi-directional many-to-one association to File
	@OneToMany(mappedBy="commit", fetch=FetchType.LAZY)
	private List<File> files;

	public Commit() {
	}
	
	/**
	 * @return the sha
	 */
	public String getSha() {
		return sha;
	}

	/**
	 * @param sha the sha to set
	 */
	public void setSha(String sha) {
		this.sha = sha;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the committer
	 */
	public Committer getCommitter() {
		return committer;
	}

	/**
	 * @param committer the committer to set
	 */
	public void setCommitter(Committer committer) {
		this.committer = committer;
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
	 * @return the files
	 */
	public List<File> getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(List<File> files) {
		this.files = files;
	}

	/**
	 * 
	 * @param file
	 * @return file added
	 */
	public File addFile(File file) {
		getFiles().add(file);
		file.setCommit(this);

		return file;
	}

	/**
	 * 
	 * @param file
	 * @return file removed
	 */
	public File removeFile(File file) {
		getFiles().remove(file);
		file.setCommit(null);

		return file;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sha == null) ? 0 : sha.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Commit other = (Commit) obj;
		if (sha == null) {
			if (other.sha != null)
				return false;
		} else if (!sha.equals(other.sha))
			return false;
		return true;
	}
	
}