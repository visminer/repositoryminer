package org.repositoryminer.model;

import java.util.Date;
import java.util.List;

import org.bson.Document;

/**
 * This class represents the "commit" object in the database. This class
 * represents a commit.
 */
public class Commit {

	private String id;
	private String shortMessage;
	private String fullMessage;
	private Date authoredDate;
	private Date commitDate;
	private String repository;
	private List<String> parents;
	private Contributor author;
	private Contributor committer;
	private List<Diff> diffs;

	public Commit() {
	}

	public Document toDocument() {
		Document doc = new Document();
		doc.append("_id", id).append("short_message", shortMessage).append("full_message", fullMessage).
		append("authored_date", authoredDate).append("commit_date", commitDate).append("repository", repository).
		append("parents", parents).append("author", author.toDBObject()).append("committer", committer.toDBObject()).
		append("diffs", Diff.toDBList(diffs));
		return doc;
	}

	public Commit(String id, String shortMessage, String fullMessage, Date authoredDate, Date commitDate,
			String repository, List<String> parents, Contributor author, Contributor committer, List<Diff> diffs) {
		super();
		this.id = id;
		this.shortMessage = shortMessage;
		this.fullMessage = fullMessage;
		this.authoredDate = authoredDate;
		this.commitDate = commitDate;
		this.repository = repository;
		this.parents = parents;
		this.author = author;
		this.committer = committer;
		this.diffs = diffs;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the shortMessage
	 */
	public String getShortMessage() {
		return shortMessage;
	}

	/**
	 * @param shortMessage
	 *            the shortMessage to set
	 */
	public void setShortMessage(String shortMessage) {
		this.shortMessage = shortMessage;
	}

	/**
	 * @return the fullMessage
	 */
	public String getFullMessage() {
		return fullMessage;
	}

	/**
	 * @param fullMessage
	 *            the fullMessage to set
	 */
	public void setFullMessage(String fullMessage) {
		this.fullMessage = fullMessage;
	}

	/**
	 * @return the authoredDate
	 */
	public Date getAuthoredDate() {
		return authoredDate;
	}

	/**
	 * @param authoredDate
	 *            the authoredDate to set
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
	 * @param commitDate
	 *            the commitDate to set
	 */
	public void setCommitDate(Date commitDate) {
		this.commitDate = commitDate;
	}

	/**
	 * @return the repository
	 */
	public String getRepository() {
		return repository;
	}

	/**
	 * @param repository
	 *            the repository to set
	 */
	public void setRepository(String repository) {
		this.repository = repository;
	}

	/**
	 * @return the parents
	 */
	public List<String> getParents() {
		return parents;
	}

	/**
	 * @param parents
	 *            the parents to set
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
	 * @param author
	 *            the author to set
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
	 * @param committer
	 *            the committer to set
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
	 * @param diffs
	 *            the diffs to set
	 */
	public void setDiffs(List<Diff> diffs) {
		this.diffs = diffs;
	}

}