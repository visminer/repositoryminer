package br.edu.ufba.softvis.visminer.model.business;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.FileDB;

public class File {
	
	private int id;
	private String path;
	private String uid;
	private FileState fileState;
	private List<SoftwareUnit> softwareUnits;
	
	public File(){}
	
	public File(int id, String path, String uid) {
		super();
		this.id = id;
		this.path = path;
		this.uid = uid;
	}

	public File(FileDB fileDb){
		this.id = fileDb.getId();
		this.path = fileDb.getPath();
		this.uid = fileDb.getUid();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public FileState getFileState() {
		return fileState;
	}

	public void setFileState(FileState fileState) {
		this.fileState = fileState;
	}

	public List<SoftwareUnit> getSoftwareUnits() {
		return softwareUnits;
	}

	public void setSoftwareUnits(List<SoftwareUnit> softwareUnits) {
		this.softwareUnits = softwareUnits;
	}
	
}