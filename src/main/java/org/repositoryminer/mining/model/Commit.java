package org.repositoryminer.mining.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

public class Commit {

	private String id;
	private String message;
	private Date authoredDate;
	private Date commitDate;
	private List<String> parents;
	private Contributor author;
	private Contributor committer;
	private List<Diff> diffs;
	
	@SuppressWarnings("unchecked")
	public static List<Commit> parseDocuments(List<Document> commitsDocs) {
		List<Commit> commits = new ArrayList<Commit>();
		for (Document doc : commitsDocs) {
			List<String> parents = new ArrayList<String>();
			for (String p : (List<String>) doc.get("parents")) {
				parents.add(p);
			}
			Commit c = new Commit(doc.getString("_id"), doc.getString("message"), 
					doc.getDate("authored_date"), doc.getDate("commit_date"), parents,
					Contributor.parseDocument((Document) doc.get("author")), 
					Contributor.parseDocument((Document) doc.get("committer")),
					Diff.parseDocuments((List<Document>) doc.get("diffs")));
			commits.add(c);
		}
		return commits;
	}
	
	public Commit() {}
	
	public Commit(String id, String message, Date authoredDate, Date commitDate, List<String> parents,
			Contributor author, Contributor committer, List<Diff> diffs) {
		super();
		this.id = id;
		this.message = message;
		this.authoredDate = authoredDate;
		this.commitDate = commitDate;
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