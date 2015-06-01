package br.edu.ufba.softvis.visminer.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the software_unit_x_commit database table.
 * 
 */
@Embeddable
public class SoftwareUnitXCommitPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="software_unit_id", insertable=false, updatable=false, unique=true, nullable=false)
	private int softwareUnitId;

	@Column(name="commit_id", insertable=false, updatable=false, unique=true, nullable=false)
	private int commitId;

	public SoftwareUnitXCommitPK() {
	}
	public int getSoftwareUnitId() {
		return this.softwareUnitId;
	}
	public void setSoftwareUnitId(int softwareUnitId) {
		this.softwareUnitId = softwareUnitId;
	}
	public int getCommitId() {
		return this.commitId;
	}
	public void setCommitId(int commitId) {
		this.commitId = commitId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SoftwareUnitXCommitPK)) {
			return false;
		}
		SoftwareUnitXCommitPK castOther = (SoftwareUnitXCommitPK)other;
		return 
			(this.softwareUnitId == castOther.softwareUnitId)
			&& (this.commitId == castOther.commitId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.softwareUnitId;
		hash = hash * prime + this.commitId;
		
		return hash;
	}
}