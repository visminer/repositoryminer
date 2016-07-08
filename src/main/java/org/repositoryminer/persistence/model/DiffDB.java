package org.repositoryminer.persistence.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.scm.DiffType;

/**
 * This class represents the "change" object in the database. This class
 * represents the changes made in a commit.
 */
public class DiffDB {

	private String path;
	private String oldPath;
	private String hash;
	private int linesAdded;
	private int linesRemoved;
	private DiffType type;

	public DiffDB() {
	}

	public static List<Document> toDocumentList(List<DiffDB> diffs) {
		List<Document> list = new ArrayList<Document>();
		for (DiffDB d : diffs) {
			Document doc = new Document();
			doc.append("path", d.getPath()).append("old_path", d.getOldPath()).append("hash", d.getHash())
					.append("lines_added", d.getLinesAdded()).append("lines_removed", d.getLinesRemoved())
					.append("type", d.getType().toString());
			list.add(doc);
		}
		return list;
	}

	public DiffDB(String path, String oldPath, String hash, int linesAdded, int linesRemoved, DiffType type) {
		super();
		this.path = path;
		this.oldPath = oldPath;
		this.hash = hash;
		this.linesAdded = linesAdded;
		this.linesRemoved = linesRemoved;
		this.type = type;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the oldPath
	 */
	public String getOldPath() {
		return oldPath;
	}

	/**
	 * @param oldPath
	 *            the oldPath to set
	 */
	public void setOldPath(String oldPath) {
		this.oldPath = oldPath;
	}

	/**
	 * @return the linesAdded
	 */
	public int getLinesAdded() {
		return linesAdded;
	}

	/**
	 * @param linesAdded
	 *            the linesAdded to set
	 */
	public void setLinesAdded(int linesAdded) {
		this.linesAdded = linesAdded;
	}

	/**
	 * @return the linesRemoved
	 */
	public int getLinesRemoved() {
		return linesRemoved;
	}

	/**
	 * @param linesRemoved
	 *            the linesRemoved to set
	 */
	public void setLinesRemoved(int linesRemoved) {
		this.linesRemoved = linesRemoved;
	}

	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * @param hash
	 *            the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * @return the type
	 */
	public DiffType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(DiffType type) {
		this.type = type;
	}

}