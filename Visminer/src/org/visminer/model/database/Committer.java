package org.visminer.model.database;

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
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private int id;

	@Column(name="email", length=255, nullable=false)
	private String email;

	@Column(name="name", length=255, nullable=false)
	private String name;

	//bi-directional many-to-one association to Commit
	@OneToMany(mappedBy="committer")
	private List<Commit> commits;

	//bi-directional many-to-many association to Repository
	@ManyToMany(mappedBy="committers")
	private List<Repository> repositories;

	public Committer() {
	}

	public Committer(String email, String name) {
		this.email = email;
		this.name = name;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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
		return this.repositories;
	}

	public void setRepositories(List<Repository> repositories) {
		this.repositories = repositories;
	}

	public void addRepository(Repository repository){
		this.getRepositories().add(repository);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Committer other = (Committer) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}