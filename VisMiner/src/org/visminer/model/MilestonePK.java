package org.visminer.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the milestone database table.
 * 
 */
@Embeddable
public class MilestonePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="number")
	private int number;

	@Column(name="repository_id_git", insertable=false, updatable=false)
	private String repositoryIdGit;

	public MilestonePK() {
	}
	public int getNumber() {
		return this.number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getRepositoryIdGit() {
		return this.repositoryIdGit;
	}
	public void setRepositoryIdGit(String repositoryIdGit) {
		this.repositoryIdGit = repositoryIdGit;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MilestonePK)) {
			return false;
		}
		MilestonePK castOther = (MilestonePK)other;
		return 
			(this.number == castOther.number)
			&& this.repositoryIdGit.equals(castOther.repositoryIdGit);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.number;
		hash = hash * prime + this.repositoryIdGit.hashCode();
		
		return hash;
	}
}