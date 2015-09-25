package br.edu.ufba.softvis.visminer.model.business;

/**
 * User friendly file state bean class.
 * This class will be used for user interface.
 */

public class FileState {

	private int linesAdded;
	private int linesRemoved;
	private boolean deleted;
	
	public FileState(){}

	/**
	 * @param linesAdded
	 * @param linesRemoved
	 * @param deleted
	 */
	public FileState(int linesAdded, int linesRemoved, boolean deleted) {
		super();
		this.linesAdded = linesAdded;
		this.linesRemoved = linesRemoved;
		this.deleted = deleted;
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
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
}