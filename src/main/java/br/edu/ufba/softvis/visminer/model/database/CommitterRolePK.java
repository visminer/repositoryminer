package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the committer_role database table.
 * 
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
	
	public CommitterRolePK(int committerId, int repositoryId) {
		super();
		this.committerId = committerId;
		this.repositoryId = repositoryId;
	}

	public int getCommitterId() {
		return this.committerId;
	}
	public void setCommitterId(int committerId) {
		this.committerId = committerId;
	}
	public int getRepositoryId() {
		return this.repositoryId;
	}
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