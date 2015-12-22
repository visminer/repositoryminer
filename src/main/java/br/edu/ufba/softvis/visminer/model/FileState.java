package br.edu.ufba.softvis.visminer.model;

import br.edu.ufba.softvis.visminer.constant.ChangeType;

/**
 * User friendly file state bean class.
 * This class will be used for user interface.
 */

public class FileState {

	private int linesAdded;
	private int linesRemoved;
	private ChangeType change;
	
	public FileState(){}

	/**
	 * @param linesAdded
	 * @param linesRemoved
	 * @param change
	 */
	public FileState(int linesAdded, int linesRemoved, ChangeType change) {
		super();
		this.linesAdded = linesAdded;
		this.linesRemoved = linesRemoved;
		this.change = change;
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
	 * @return the change
	 */
	public ChangeType getChange() {
		return change;
	}

	/**
	 * @param change the change to set
	 */
	public void setChange(ChangeType change) {
		this.change = change;
	}

	
}