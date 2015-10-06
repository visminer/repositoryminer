package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.edu.ufba.softvis.visminer.constant.IssueCommandType;
import br.edu.ufba.softvis.visminer.model.business.Issue;
import br.edu.ufba.softvis.visminer.model.business.IssueCommand;

/**
 * The persistent class for the commit_reference_issue database table.
 */
@Entity
@Table(name="commit_reference_issue")
@NamedQueries({
	@NamedQuery(name="CommitReferenceIssueDB.findByCommit", query="select cri from CommitReferenceIssueDB"
			+ " cri where cri.commit.id = :commitId" )
})
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
	public CommitReferenceIssueDB(CommitReferenceIssuePK id, IssueCommandType command) {
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
	public IssueCommandType getCommand() {
		return IssueCommandType.parse(command);
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(IssueCommandType command) {
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

	public static List<IssueCommand> toBusiness(List<CommitReferenceIssueDB> cris){
		
		List<IssueCommand> commands = new ArrayList<IssueCommand>();
		
		if(cris == null)
			return commands;
		
		for(CommitReferenceIssueDB cri : cris){
			
			IssueCommand ic = new IssueCommand(cri.getCommand());
			
			Issue i = new Issue();
			i.setId(cri.getId().getIssueId());
			ic.setIssue(i);
			
			commands.add(ic);
			
		}
		
		return commands;
		
	}
	
}