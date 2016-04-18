package org.repositoryminer.model;

import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

/**
 * This class represents the "change" object in the database.
 * This class represents the changes made in a commit.
 */
public class Diff {

	private String newPath;
	private String oldPath;
	private int linesAdded;
	private int linesRemoved;
	private DiffType type;
	
	public Diff(){}
	
	public static BasicDBList toDBList(List<Diff> diffs){
		BasicDBList list = new BasicDBList();
		for(Diff d : diffs){
			BasicDBObject obj = new BasicDBObject();
			obj.append("new_path", d.getNewPath()).append("old_path", d.getOldPath()).append("lines_added", d.getLinesAdded()).
			append("lines_removed", d.getLinesRemoved()).append("type", d.getType().getKey());
			list.add(obj);
		}
		return list;
	}
	
	public Diff(String newPath, String oldPath, int linesAdded, int linesRemoved, DiffType type) {
		super();
		this.newPath = newPath;
		this.oldPath = oldPath;
		this.linesAdded = linesAdded;
		this.linesRemoved = linesRemoved;
		this.type = type;
	}

	/**
	 * @return the newPath
	 */
	public String getNewPath() {
		return newPath;
	}

	/**
	 * @param newPath the newPath to set
	 */
	public void setNewPath(String newPath) {
		this.newPath = newPath;
	}

	/**
	 * @return the oldPath
	 */
	public String getOldPath() {
		return oldPath;
	}
	/**
	 * @param oldPath the oldPath to set
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
	 * @param linesAdded the linesAdded to set
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
	 * @param linesRemoved the linesRemoved to set
	 */
	public void setLinesRemoved(int linesRemoved) {
		this.linesRemoved = linesRemoved;
	}
	/**
	 * @return the type
	 */
	public DiffType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(DiffType type) {
		this.type = type;
	}

}