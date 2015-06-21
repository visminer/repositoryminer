package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;
import javax.persistence.*;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * The primary key class for the software_unit_x_commit database table.
 */
@Embeddable
public class SoftwareUnitXCommitPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="software_unit_id", insertable=true, updatable=false, nullable=false)
	private int softwareUnitId;

	@Column(name="commit_id", insertable=true, updatable=false, nullable=false)
	private int commitId;

	public SoftwareUnitXCommitPK() {
	}

	/**
	 * @param softwareUnitId
	 * @param commitId
	 */
	public SoftwareUnitXCommitPK(int softwareUnitId, int commitId) {
		super();
		this.softwareUnitId = softwareUnitId;
		this.commitId = commitId;
	}

	/**
	 * @return the softwareUnitId
	 */
	public int getSoftwareUnitId() {
		return softwareUnitId;
	}

	/**
	 * @param softwareUnitId the softwareUnitId to set
	 */
	public void setSoftwareUnitId(int softwareUnitId) {
		this.softwareUnitId = softwareUnitId;
	}

	/**
	 * @return the commitId
	 */
	public int getCommitId() {
		return commitId;
	}

	/**
	 * @param commitId the commitId to set
	 */
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