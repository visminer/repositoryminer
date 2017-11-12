package org.repositoryminer.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Represents a commit.
 */
public class Commit {

	private ObjectId id;
	private String hash;
	private Developer author;
	private Developer committer;
	private String message;
	private List<Change> changes;
	private List<String> parents;
	private Date authorDate;
	private Date committerDate;
	private boolean merge;
	private ObjectId repository;

	/**
	 * Converts database documents to commits.
	 * 
	 * @param documents
	 *
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
	 * 
	 * @return a commit.
	 */
	@SuppressWarnings("unchecked")
	public static Commit parseDocument(Document document) {
		Commit commit = new Commit(document.getObjectId("_id"),
				document.getString("hash"), Developer.parseDocument(document.get("author", Document.class)),
				Developer.parseDocument(document.get("committer", Document.class)), document.getString("message"),
				Change.parseDocuments(document.get("changes", List.class)), document.get("parents", List.class),
				document.getDate("author_date"), document.getDate("committer_date"),
				document.getBoolean("merge", false), document.getObjectId("repository"));
		return commit;
	}

	/**
	 * Converts the commit to a document.
	 * 
	 * @return a document.
	 */
	public Document toDocument() {
		Document doc = new Document();
		doc.append("hash", hash).
			append("author", author.toDocument()).
			append("committer", committer.toDocument()).
			append("message", message).
			append("changes", Change.toDocumentList(changes)).
			append("parents", parents).
			append("author_date", authorDate.getTime()).
			append("committer_date", committerDate.getTime()).
			append("merge", merge).
			append("repository", repository);
		return doc;
	}

	public Commit() {}
	
	public Commit(String hash, Date committerDate) {
		this.hash = hash;
		this.committerDate = committerDate;
	}

	public Commit(ObjectId id, String hash, Developer author, Developer committer, String message, List<Change> changes,
			List<String> parents, Date authorDate, Date committerDate, boolean merge, ObjectId repository) {
		super();
		this.id = id;
		this.hash = hash;
		this.author = author;
		this.committer = committer;
		this.message = message;
		this.changes = changes;
		this.parents = parents;
		this.authorDate = authorDate;
		this.committerDate = committerDate;
		this.merge = merge;
		this.repository = repository;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Developer getAuthor() {
		return author;
	}

	public void setAuthor(Developer author) {
		this.author = author;
	}

	public Developer getCommitter() {
		return committer;
	}

	public void setCommitter(Developer committer) {
		this.committer = committer;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Change> getChanges() {
		return changes;
	}

	public void setChanges(List<Change> changes) {
		this.changes = changes;
	}

	public List<String> getParents() {
		return parents;
	}

	public void setParents(List<String> parents) {
		this.parents = parents;
	}

	public Date getAuthorDate() {
		return authorDate;
	}

	public void setAuthorDate(Date authorDate) {
		this.authorDate = authorDate;
	}

	public Date getCommitterDate() {
		return committerDate;
	}

	public void setCommitterDate(Date committerDate) {
		this.committerDate = committerDate;
	}

	public boolean isMerge() {
		return merge;
	}

	public void setMerge(boolean merge) {
		this.merge = merge;
	}

	public ObjectId getRepository() {
		return repository;
	}

	public void setRepository(ObjectId repository) {
		this.repository = repository;
	}

}