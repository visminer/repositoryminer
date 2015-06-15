package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the committer_role database table.
 * 
 */
@Entity
@Table(name="committer_role")
@NamedQuery(name="CommitterRoleDB.findAll", query="SELECT c FROM CommitterRoleDB c")

public class CommitterRoleDB implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CommitterRolePK id;

	@Column(nullable=false)
	private boolean contributor;

	//bi-directional many-to-one association to CommitterDB
	@ManyToOne
	@JoinColumn(name="committer_id", nullable=false, insertable=false, updatable=false)
	private CommitterDB committer;

	//bi-directional many-to-one association to RepositoryDB
	@ManyToOne
	@JoinColumn(name="repository_id", nullable=false, insertable=false, updatable=false)
	private RepositoryDB repository;

	public CommitterRoleDB() {
	}

	public CommitterRoleDB(int repositoryId, int committerId, boolean contributor) {
		super();
		this.id = new CommitterRolePK(committerId, repositoryId);
		this.contributor = contributor;
	}

	public CommitterRolePK getId() {
		return this.id;
	}

	public void setId(CommitterRolePK id) {
		this.id = id;
	}

	public boolean getContributor() {
		return this.contributor;
	}

	public void setContribuitor(boolean contributor) {
		this.contributor = contributor;
	}

	public CommitterDB getCommitter() {
		return this.committer;
	}

	public void setCommitter(CommitterDB committer) {
		this.committer = committer;
	}

	public RepositoryDB getRepository() {
		return this.repository;
	}

	public void setRepository(RepositoryDB repository) {
		this.repository = repository;
	}

}