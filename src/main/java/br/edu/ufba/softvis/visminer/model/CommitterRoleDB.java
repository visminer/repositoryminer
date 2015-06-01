package br.edu.ufba.softvis.visminer.model;

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
	private byte contribuitor;

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

	public CommitterRolePK getId() {
		return this.id;
	}

	public void setId(CommitterRolePK id) {
		this.id = id;
	}

	public byte getContribuitor() {
		return this.contribuitor;
	}

	public void setContribuitor(byte contribuitor) {
		this.contribuitor = contribuitor;
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