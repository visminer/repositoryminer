package org.repositoryminer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * This class represents the "commit" object in the database. This class
 * represents a commit.
 */
public class Commit {

	private String id;
	private String message;
	private Date authoredDate;
	private Date commitDate;
	private String repository;
	private List<String> parents;
	private Contributor author;
	private Contributor committer;
	private List<Diff> diffs;
	private List<IssueReference> issueReferences;

	@SuppressWarnings("unchecked")
	public static List<Commit> parseDocuments(List<Document> commitsDocs) {
		List<Commit> commits = new ArrayList<Commit>();
		for (Document doc : commitsDocs) {
			List<String> parents = new ArrayList<String>();
			for (String p : (List<String>) doc.get("parents")) {
				parents.add(p);
			}
			commits.add(parseDocument(doc, parents));
		}
		return commits;
	}

	@SuppressWarnings("unchecked")
	public static Commit parseDocument(Document commitDoc, List<String> parents) {
		Commit commit = new Commit(commitDoc.getString("_id"), commitDoc.getString("message"),
				commitDoc.getDate("authored_date"), commitDoc.getDate("commit_date"),
				commitDoc.get("repository").toString(), parents,
				Contributor.parseDocument((Document) commitDoc.get("author")),
				Contributor.parseDocument((Document) commitDoc.get("committer")),
				Diff.parseDocuments((List<Document>) commitDoc.get("diffs")));

		return commit;
	}

	public static Commit parseDocument(Document commitDoc) {
		return parseDocument(commitDoc, new ArrayList<String>());
	}

	public Document toDocument() {
		Document doc = new Document();
		doc.append("_id", id).append("message", message).append("authored_date", authoredDate)
				.append("commit_date", commitDate).append("repository", new ObjectId(repository))
				.append("parents", parents).append("author", author.toDocument())
				.append("committer", committer.toDocument()).append("diffs", Diff.toDocumentList(diffs));
		return doc;
	}

	public Commit() {
	}

	public Commit(String id) {
		super();
		this.id = id;
	}

	public Commit(String id, String message, Date authoredDate, Date commitDate, String repository,
			List<String> parents, Contributor author, Contributor committer, List<Diff> diffs) {
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

	public Contributor getAuthor() {
		return author;
	}

	public void setAuthor(Contributor author) {
		this.author = author;
	}

	public Contributor getCommitter() {
		return committer;
	}

	public void setCommitter(Contributor committer) {
		this.committer = committer;
	}

	public List<Diff> getDiffs() {
		return diffs;
	}

	public void setDiffs(List<Diff> diffs) {
		this.diffs = diffs;
	}

	public List<IssueReference> getIssueReferences() {
		return issueReferences;
	}

	public void setIssueReferences(List<IssueReference> issueReferences) {
		this.issueReferences = issueReferences;
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