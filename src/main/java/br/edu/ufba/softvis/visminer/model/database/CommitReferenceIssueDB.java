package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.edu.ufba.softvis.visminer.constant.IssueCommand;

/**
 * The persistent class for the commit_reference_issue database table.
 */
@Entity
@Table(name="commit_reference_issue")
public class CommitReferenceIssueDB implements Serializable{

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CommitReferenceIssuePK id;
	
	@Column(name="command")
	private int command;
	
	@ManyToOne
	@JoinColumn(name="commit_id", nullable=false, insertable=false, updatable=false)
	private CommitDB commit;

	@ManyToOne
	@JoinColumn(name="issue_id", nullable=false, insertable=false, updatable=false)
	private IssueDB issue;
	
	public CommitReferenceIssueDB(){}

	/**
	 * @param id
	 * @param command
	 */
	public CommitReferenceIssueDB(CommitReferenceIssuePK id, IssueCommand command) {
		super();
		this.id = id;
		this.command = command != null ? command.getId() : 0;
	}

	/**
	 * @return the id
	 */
	public CommitReferenceIssuePK getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(CommitReferenceIssuePK id) {
		this.id = id;
	}

	/**
	 * @return the command
	 */
	public IssueCommand getCommand() {
		return IssueCommand.parse(command);
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(IssueCommand command) {
		this.command = command != null ? command.getId() : 0;
	}

	/**
	 * @return the commit
	 */
	public CommitDB getCommit() {
		return commit;
	}

	/**
	 * @param commit the commit to set
	 */
	public void setCommit(CommitDB commit) {
		this.commit = commit;
	}

	/**
	 * @return the issue
	 */
	public IssueDB getIssue() {
		return issue;
	}

	/**
	 * @param issue the issue to set
	 */
	public void setIssue(IssueDB issue) {
		this.issue = issue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		CommitReferenceIssueDB other = (CommitReferenceIssueDB) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}