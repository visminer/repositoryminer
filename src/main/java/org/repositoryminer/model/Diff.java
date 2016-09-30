package org.repositoryminer.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.scm.DiffType;

/**
 * This class represents the "change" object in the database. This class
 * represents the changes made in a commit.
 */
public class Diff {

	private String path;
	private String oldPath;
	private long hash;
	private int linesAdded;
	private int linesRemoved;
	private DiffType type;

	public static List<Diff> parseDocuments(List<Document> docs) {
		List<Diff> diffs = new ArrayList<Diff>();
		for (Document doc : docs) {
			Diff diff = new Diff(doc.getString("path"), doc.getString("old_path"),
				doc.getLong("hash"), doc.getInteger("lines_added", 0), doc.getInteger("lines_removed", 0),
				DiffType.valueOf(doc.getString("type")));
			diffs.add(diff);
		}
		return diffs;
	}

	public static List<Document> toDocumentList(List<Diff> diffs) {
		List<Document> list = new ArrayList<Document>();
		for (Diff d : diffs) {
			Document doc = new Document();
			doc.append("path", d.getPath()).append("old_path", d.getOldPath()).append("hash", d.getHash())
					.append("lines_added", d.getLinesAdded()).append("lines_removed", d.getLinesRemoved())
					.append("type", d.getType().toString());
			list.add(doc);
		}
		return list;
	}

	public Diff() {
	}

	public Diff(String path, String oldPath, long hash, int linesAdded, int linesRemoved, DiffType type) {
		super();
		this.path = path;
		this.oldPath = oldPath;
		this.hash = hash;
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

	public long getHash() {
		return hash;
	}

	public void setHash(long hash) {
		this.hash = hash;
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

	public DiffType getType() {
		return type;
	}

	public void setType(DiffType type) {
		this.type = type;
	}

}