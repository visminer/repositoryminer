package br.edu.ufba.softvis.visminer.model.bean;

public class FileState {

	private int linesAdded;
	private int linesRemoved;
	private boolean deleted;
	
	public FileState(){}
	
	public FileState(int linesAdded, int linesRemoved, boolean deleted) {
		super();
		this.linesAdded = linesAdded;
		this.linesRemoved = linesRemoved;
		this.deleted = deleted;
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

	public boolean isRemoved() {
		return deleted;
	}

	public void setRemoved(boolean deleted) {
		this.deleted = deleted;
	}
	
}
