package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;
import javax.persistence.*;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * The primary key class for the committer_role database table.
 */
@Embeddable
public class CommitterRolePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="committer_id", insertable=true, updatable=false, nullable=false)
	private int committerId;

	@Column(name="repository_id", insertable=true, updatable=false, nullable=false)
	private int repositoryId;

	public CommitterRolePK() {
	}

	/**
	 * @param committerId
	 * @param repositoryId
	 */
	public CommitterRolePK(int committerId, int repositoryId) {
		super();
		this.committerId = committerId;
		this.repositoryId = repositoryId;
	}

	/**
	 * @return the committerId
	 */
	public int getCommitterId() {
		return committerId;
	}

	/**
	 * @param committerId the committerId to set
	 */
	public void setCommitterId(int committerId) {
		this.committerId = committerId;
	}

	/**
	 * @return the repositoryId
	 */
	public int getRepositoryId() {
		return repositoryId;
	}

	/**
	 * @param repositoryId the repositoryId to set
	 */
	public void setRepositoryId(int repositoryId) {
		this.repositoryId = repositoryId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CommitterRolePK)) {
			return false;
		}
		CommitterRolePK castOther = (CommitterRolePK)other;
		return 
			(this.committerId == castOther.committerId)
			&& (this.repositoryId == castOther.repositoryId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.committerId;
		hash = hash * prime + this.repositoryId;
		
		return hash;
	}
}