package org.visminer.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the committer database table.
 * 
 */
@Entity
@Table(name="committer")
@NamedQuery(name="Committer.findAll", query="SELECT c FROM Committer c")
public class Committer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="email", length=255)
	private String email;

	@Column(name="name", nullable=false, length=90)
	private String name;

	//bi-directional many-to-one association to Commit
	@OneToMany(mappedBy="committer")
	private List<Commit> commits;

	//bi-directional many-to-many association to Repository
	@ManyToMany
	@JoinTable(name="committer_contributes_repository",
		joinColumns = {
			@JoinColumn(name="committer_email", nullable=false, referencedColumnName = "email")
		},
		inverseJoinColumns={
			@JoinColumn(name = "repository_id_git", nullable=false ,referencedColumnName = "id_git"),
		}
	)
	private List<Repository> repositories;

	public Committer() {
	}

	public Committer(String email, String name) {
		super();
		this.email = email;
		this.name = name;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Commit> getCommits() {
		return this.commits;
	}

	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}

	public Commit addCommit(Commit commit) {
		getCommits().add(commit);
		commit.setCommitter(this);

		return commit;
	}

	public Commit removeCommit(Commit commit) {
		getCommits().remove(commit);
		commit.setCommitter(null);

		return commit;
	}

	public List<Repository> getRepositories() {
		return repositories;
	}

	public void setRepositories(List<Repository> repositories) {
		this.repositories = repositories;
	}


}