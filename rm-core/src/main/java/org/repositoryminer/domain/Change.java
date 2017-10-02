package org.repositoryminer.domain;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

/**
 * This class represents the changes made in a commit.
 */
public class Change {

	private String path;
	private String oldPath;
	private int linesAdded;
	private int linesRemoved;
	private ChangeType type;

	/**
	 * Converts documents to changes.
	 * 
	 * @param documents
	 *            the documents
	 * @return a list of changes.
	 */
	public static List<Change> parseDocuments(List<Document> documents) {
		List<Change> changes = new ArrayList<Change>();
		if (documents == null)
			return changes;

		for (Document doc : documents) {
			Change change = new Change(doc.getString("path"), doc.getString("old_path"),
					doc.getInteger("lines_added", 0), doc.getInteger("lines_removed", 0),
					ChangeType.valueOf(doc.getString("type")));
			changes.add(change);
		}
		return changes;
	}

	/**
	 * Converts changes to documents.
	 * 
	 * @param changes
	 *            the changes.
	 * @return a list of documents.
	 */
	public static List<Document> toDocumentList(List<Change> changes) {
		List<Document> list = new ArrayList<Document>();
		for (Change c : changes) {
			Document doc = new Document();
			doc.append("path", c.getPath()).append("old_path", c.getOldPath())
			.append("lines_added", c.getLinesAdded()).append("lines_removed", c.getLinesRemoved())
			.append("type", c.getType().toString());
			list.add(doc);
		}
		return list;
	}

	public Change() {
	}

	public Change(String path, String oldPath, int linesAdded, int linesRemoved, ChangeType type) {
		this.path = path;
		this.oldPath = oldPath;
		this.linesAdded = linesAdded;
		this.linesRemoved = linesRemoved;
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getOldPath() {
		return oldPath;
	}

	public void setOldPath(String oldPath) {
		this.oldPath = oldPath;
	}

	public int getLinesAdded() {
		return linesAdded;
	}

	public void setLinesAdded(int linesAdded) {
		this.linesAdded = linesAdded;
	}

	public int getLinesRemoved() {
		return linesRemoved;
	}

	public void setLinesRemoved(int linesRemoved) {
		this.linesRemoved = linesRemoved;
	}

	public ChangeType getType() {
		return type;
	}

	public void setType(ChangeType type) {
		this.type = type;
	}

}