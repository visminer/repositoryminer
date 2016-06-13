package org.repositoryminer.mining.model;

import org.repositoryminer.scm.DiffType;

public class Diff {

	private String path;
	private String oldPath;
	private String hash;
	private int linesAdded;
	private int linesRemoved;
	private DiffType type;
	
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
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
	 * @param oldPath the oldPath to set
	 */
	public void setOldPath(String oldPath) {
		this.oldPath = oldPath;
	}
	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}
	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
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