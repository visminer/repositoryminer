package org.repositoryminer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * This class represents a commit.
 */
public class Commit {

	private String id;
	private String message;
	private Date authoredDate;
	private Date commitDate;
	private String repository;
	private List<String> parents;
	private boolean merge;
	private PersonIdent author;
	private PersonIdent committer;
	private List<Change> diffs;

	/**
	 * Converts database documents to commits.
	 * 
	 * @param documents
	 *            the documents from database.
	 * @return a list of commits.
	 */
	public static List<Commit> parseDocuments(List<Document> documents) {
		List<Commit> commits = new ArrayList<Commit>();
		for (Document doc : documents) {
			commits.add(parseDocument(doc));
		}
		return commits;
	}

	/**
	 * Converts a document to a commit.
	 * 
	 * @param document
	 *            the document.
	 * @return a commit.
	 */
	@SuppressWarnings("unchecked")
	public static Commit parseDocument(Document document) {
		Commit commit = new Commit(document.getString("_id"), document.getString("message"),
				document.getDate("authored_date"), document.getDate("commit_date"), null,
				document.get("parents", List.class), document.getBoolean("merge", false),
				PersonIdent.parseDocument(document.get("author", Document.class)),
				PersonIdent.parseDocument(document.get("committer", Document.class)),
				Change.parseDocuments(document.get("diffs", List.class)));

		commit.setRepository(document.get("repository") != null ? document.get("repository").toString() : "");
		return commit;
	}

	/**
	 * Converts the commit to a document.
	 * 
	 * @return a document.
	 */
	public Document toDocument() {
		Document doc = new Document();
		doc.append("_id", id).append("message", message).append("authored_date", authoredDate)
		.append("commit_date", commitDate).append("repository", new ObjectId(repository))
		.append("parents", parents).append("merge", merge).append("author", author.toDocument())
		.append("committer", committer.toDocument()).append("diffs", Change.toDocumentList(diffs));
		return doc;
	}

	public Commit() {
	}

	public Commit(String id, String message, Date authoredDate, Date commitDate, String repository,
			List<String> parents, boolean merge, PersonIdent author, PersonIdent committer, List<Change> diffs) {
		this.id = id;
		this.message = message;
		this.authoredDate = authoredDate;
		this.commitDate = commitDate;
		this.repository = repository;
		this.parents = parents;
		this.merge = merge;
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

	public boolean isMerge() {
		return merge;
	}

	public void setMerge(boolean merge) {
		this.merge = merge;
	}

	public PersonIdent getAuthor() {
		return author;
	}

	public void setAuthor(PersonIdent author) {
		this.author = author;
	}

	public PersonIdent getCommitter() {
		return committer;
	}

	public void setCommitter(PersonIdent committer) {
		this.committer = committer;
	}

	public List<Change> getDiffs() {
		return diffs;
	}

	public void setDiffs(List<Change> diffs) {
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
		Commit other = (Commit) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}