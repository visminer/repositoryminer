package br.edu.ufba.softvis.visminer.model.business;

import br.edu.ufba.softvis.visminer.model.database.FileXCommitDB;

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
	
	public FileState(FileXCommitDB fileXCommitDb){
		this.linesAdded = fileXCommitDb.getLinesAdded();
		this.linesRemoved = fileXCommitDb.getLinesRemoved();
		this.deleted = fileXCommitDb.getRemoved();
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