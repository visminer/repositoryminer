package org.repositoryminer.persistence.model;

import java.util.Date;
import java.util.List;

import org.bson.Document;

/**
 * This class represents the "commit" object in the database. This class
 * represents a commit.
 */
public class CommitDB {

	private String id;
	private String message;
	private Date authoredDate;
	private Date commitDate;
	private String repository;
	private List<String> parents;
	private ContributorDB author;
	private ContributorDB committer;
	private List<DiffDB> diffs;

	public CommitDB() {
	}

	public Document toDocument() {
		Document doc = new Document();
		doc.append("_id", id).append("message", message).append("authored_date", authoredDate)
				.append("commit_date", commitDate).append("repository", repository).append("parents", parents)
				.append("author", author.toDocument()).append("committer", committer.toDocument())
				.append("diffs", DiffDB.toDocumentList(diffs));
		return doc;
	}

	public CommitDB(String id) {
		super();
		this.id = id;
	}

	public CommitDB(String id, String message, Date authoredDate, Date commitDate, String repository,
			List<String> parents, ContributorDB author, ContributorDB committer, List<DiffDB> diffs) {
		super();
		this.id = id;
		this.message = message;
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
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
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
	public ContributorDB getAuthor() {
		return author;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(ContributorDB author) {
		this.author = author;
	}

	/**
	 * @return the committer
	 */
	public ContributorDB getCommitter() {
		return committer;
	}

	/**
	 * @param committer
	 *            the committer to set
	 */
	public void setCommitter(ContributorDB committer) {
		this.committer = committer;
	}

	/**
	 * @return the diffs
	 */
	public List<DiffDB> getDiffs() {
		return diffs;
	}

	/**
	 * @param diffs
	 *            the diffs to set
	 */
	public void setDiffs(List<DiffDB> diffs) {
		this.diffs = diffs;
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
		CommitDB other = (CommitDB) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}