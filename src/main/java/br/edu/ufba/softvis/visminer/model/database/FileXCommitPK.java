package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;
import javax.persistence.*;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * The primary key class for the file_x_commit database table.
 */
@Embeddable
public class FileXCommitPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="file_id", insertable=true, updatable=false, nullable=false)
	private int fileId;

	@Column(name="commit_id", insertable=true, updatable=false, nullable=false)
	private int commitId;

	public FileXCommitPK() {
	}
	
	/**
	 * @param fileId
	 * @param commitId
	 */
	public FileXCommitPK(int fileId, int commitId) {
		super();
		this.fileId = fileId;
		this.commitId = commitId;
	}


	/**
	 * @return the fileId
	 */
	public int getFileId() {
		return fileId;
	}

	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(int fileId) {
		this.fileId = fileId;
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