package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;
import javax.persistence.*;


/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * The persistent class for the committer_role database table.
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

	/**
	 * @param id
	 * @param contributor
	 */
	public CommitterRoleDB(CommitterRolePK id, boolean contributor) {
		super();
		this.id = id;
		this.contributor = contributor;
	}

	/**
	 * @return the id
	 */
	public CommitterRolePK getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(CommitterRolePK id) {
		this.id = id;
	}

	/**
	 * @return the contributor
	 */
	public boolean isContributor() {
		return contributor;
	}

	/**
	 * @param contributor the contributor to set
	 */
	public void setContributor(boolean contributor) {
		this.contributor = contributor;
	}

	/**
	 * @return the committer
	 */
	public CommitterDB getCommitter() {
		return committer;
	}

	/**
	 * @param committer the committer to set
	 */
	public void setCommitter(CommitterDB committer) {
		this.committer = committer;
	}

	/**
	 * @return the repository
	 */
	public RepositoryDB getRepository() {
		return repository;
	}

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(RepositoryDB repository) {
		this.repository = repository;
	}

}