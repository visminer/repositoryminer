package org.repositoryminer.mining.model;

import java.util.Date;
import java.util.List;

public class Commit {

	private String id;
	private String message;
	private Date authoredDate;
	private Date commitDate;
	private List<String> parents;
	private Contributor author;
	private Contributor committer;
	private List<Diff> diffs;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the authoredDate
	 */
	public Date getAuthoredDate() {
		return authoredDate;
	}
	/**
	 * @param authoredDate the authoredDate to set
	 */
	public void setAuthoredDate(Date authoredDate) {
		this.authoredDate = authoredDate;
	}
	/**
	 * @return the commitDate
	 */
	public Date getCommitDate() {
		return commitDate;
	}
	/**
	 * @param commitDate the commitDate to set
	 */
	public void setCommitDate(Date commitDate) {
		this.commitDate = commitDate;
	}
	/**
	 * @return the parents
	 */
	public List<String> getParents() {
		return parents;
	}
	/**
	 * @param parents the parents to set
	 */
	public void setParents(List<String> parents) {
		this.parents = parents;
	}
	/**
	 * @return the author
	 */
	public Contributor getAuthor() {
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(Contributor author) {
		this.author = author;
	}
	/**
	 * @return the committer
	 */
	public Contributor getCommitter() {
		return committer;
	}
	/**
	 * @param committer the committer to set
	 */
	public void setCommitter(Contributor committer) {
		this.committer = committer;
	}
	/**
	 * @return the diffs
	 */
	public List<Diff> getDiffs() {
		return diffs;
	}
	/**
	 * @param diffs the diffs to set
	 */
	public void setDiffs(List<Diff> diffs) {
		this.diffs = diffs;
	}
	
}