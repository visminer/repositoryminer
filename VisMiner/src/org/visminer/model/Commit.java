package org.visminer.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the commit database table.
 * 
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
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="committer_idcommitter", nullable=false)
	private Committer committer;

	//bi-directional many-to-many association to Issue
	@ManyToMany(fetch=FetchType.EAGER)
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
	@ManyToMany(mappedBy="commits", fetch=FetchType.EAGER)
	private List<Version> versions;

	//bi-directional many-to-one association to File
	@OneToMany(mappedBy="commit", fetch=FetchType.EAGER)
	private List<File> files;

	public Commit() {
	}

	public String getSha() {
		return this.sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Committer getCommitter() {
		return this.committer;
	}

	public void setCommitter(Committer committer) {
		this.committer = committer;
	}

	public List<Issue> getIssues() {
		return this.issues;
	}

	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}

	public List<Version> getVersions() {
		return this.versions;
	}

	public void setVersions(List<Version> versions) {
		this.versions = versions;
	}

	public List<File> getFiles() {
		return this.files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public File addFile(File file) {
		getFiles().add(file);
		file.setCommit(this);

		return file;
	}

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