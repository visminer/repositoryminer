package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the commit_reference_issue database table.
 */
@Embeddable
public class CommitReferenceIssuePK implements Serializable{

  private static final long serialVersionUID = 1L;

  @Column(name="commit_id", insertable=true, updatable=false, nullable=false)
  private int commitId;

  @Column(name="issue_id", insertable=true, updatable=false, nullable=false)
  private int issueId;

  public CommitReferenceIssuePK(){}

  /**
   * @param commitId
   * @param issueId
   */
  public CommitReferenceIssuePK(int commitId, int issueId) {
    super();
    this.commitId = commitId;
    this.issueId = issueId;
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

  /**
   * @return the issueId
   */
  public int getIssueId() {
    return issueId;
  }

  /**
   * @param issueId the issueId to set
   */
  public void setIssueId(int issueId) {
    this.issueId = issueId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + commitId;
    result = prime * result + issueId;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CommitReferenceIssuePK other = (CommitReferenceIssuePK) obj;
    if (commitId != other.commitId)
      return false;
    if (issueId != other.issueId)
      return false;
    return true;
  }

}