package org.repositoryminer.mining.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.scm.DiffType;

public class Diff {

	private String path;
	private String oldPath;
	private String hash;
	private int linesAdded;
	private int linesRemoved;
	private DiffType type;
	
	public static List<Diff> parseDocuments(List<Document> docs) {
		List<Diff> diffs = new ArrayList<Diff>();
		for (Document doc : docs) {
			Diff diff = new Diff(doc.getString("path"), doc.getString("old_path"),
				doc.getString("hash"), doc.getInteger("lines_added", 0), doc.getInteger("lines_removed", 0),
				DiffType.valueOf(doc.getString("type")));
			diffs.add(diff);
		}
		return diffs;
	}
	
	public Diff(String path, String oldPath, String hash, int linesAdded, int linesRemoved, DiffType type) {
		super();
		this.path = path;
		this.oldPath = oldPath;
		this.hash = hash;
		this.linesAdded = linesAdded;
		this.linesRemoved = linesRemoved;
		this.type = type;
	}

	public Diff() {}

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

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
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