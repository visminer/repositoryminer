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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getAuthoredDate() {
		return authoredDate;
	}

	public void setAuthoredDate(Date authoredDate) {
		this.authoredDate = authoredDate;
	}

	public Date getCommitDate() {
		return commitDate;
	}

	public void setCommitDate(Date commitDate) {
		this.commitDate = commitDate;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public List<String> getParents() {
		return parents;
	}

	public void setParents(List<String> parents) {
		this.parents = parents;
	}

	public ContributorDB getAuthor() {
		return author;
	}

	public void setAuthor(ContributorDB author) {
		this.author = author;
	}

	public ContributorDB getCommitter() {
		return committer;
	}

	public void setCommitter(ContributorDB committer) {
		this.committer = committer;
	}

	public List<DiffDB> getDiffs() {
		return diffs;
	}

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