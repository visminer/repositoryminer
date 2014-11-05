package org.visminer.model.database;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the file_x_commit database table.
 * 
 */
@Embeddable
public class FileXCommitPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="file_id", insertable=false, updatable=false)
	private int fileId;

	@Column(name="commit_id", insertable=false, updatable=false)
	private int commitId;

	public FileXCommitPK() {
	}
	public FileXCommitPK(int fileId, int commitId) {
		this.fileId = fileId;
		this.commitId = commitId;
	}
	public int getFileId() {
		return this.fileId;
	}
	public void setFileId(int fileId) {
		this.fileId = fileId;
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
		if (!(other instanceof FileXCommitPK)) {
			return false;
		}
		FileXCommitPK castOther = (FileXCommitPK)other;
		return 
			(this.fileId == castOther.fileId)
			&& (this.commitId == castOther.commitId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.fileId;
		hash = hash * prime + this.commitId;
		
		return hash;
	}
}