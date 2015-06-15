package br.edu.ufba.softvis.visminer.model.business;

import java.util.List;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		File other = (File) obj;
		if (id != other.id)
			return false;
		return true;
	}

	
}