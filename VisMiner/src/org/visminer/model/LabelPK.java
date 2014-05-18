package org.visminer.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the label database table.
 * 
 */
@Embeddable
public class LabelPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="id_label")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idLabel;

	@Column(name="issue_number", insertable=false, updatable=false)
	private int issueNumber;

	@Column(name="issue_repository_id_git", insertable=false, updatable=false)
	private String issueRepositoryIdGit;

	public LabelPK() {
	}
	public int getIdLabel() {
		return this.idLabel;
	}
	public void setIdLabel(int idLabel) {
		this.idLabel = idLabel;
	}
	public int getIssueNumber() {
		return this.issueNumber;
	}
	public void setIssueNumber(int issueNumber) {
		this.issueNumber = issueNumber;
	}
	public String getIssueRepositoryIdGit() {
		return this.issueRepositoryIdGit;
	}
	public void setIssueRepositoryIdGit(String issueRepositoryIdGit) {
		this.issueRepositoryIdGit = issueRepositoryIdGit;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof LabelPK)) {
			return false;
		}
		LabelPK castOther = (LabelPK)other;
		return 
			(this.idLabel == castOther.idLabel)
			&& (this.issueNumber == castOther.issueNumber)
			&& this.issueRepositoryIdGit.equals(castOther.issueRepositoryIdGit);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idLabel;
		hash = hash * prime + this.issueNumber;
		hash = hash * prime + this.issueRepositoryIdGit.hashCode();
		
		return hash;
	}
}