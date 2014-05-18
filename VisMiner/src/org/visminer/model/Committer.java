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
	@Column(name="id_committer")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idCommitter;

	@Column(name="email", nullable=false, length=255)
	private String email;

	@Column(name="name", nullable=false, length=90)
	private String name;

	//bi-directional many-to-one association to Commit
	@OneToMany(mappedBy="committer")
	private List<Commit> commits;

	//bi-directional many-to-one association to Repository
	@ManyToOne
	private Repository repository;

	public Committer() {
	}

	public Committer(String email, String name) {
		super();
		this.email = email;
		this.name = name;
	}

	public int getIdCommitter() {
		return this.idCommitter;
	}

	public void setIdCommitter(int idCommitter) {
		this.idCommitter = idCommitter;
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

	public Repository getRepository() {
		return this.repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

}