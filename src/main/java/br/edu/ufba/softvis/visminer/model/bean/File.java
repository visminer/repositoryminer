package br.edu.ufba.softvis.visminer.model.bean;

public class File {
	
	private int id;
	private String path;
	private String uid;
	private FileState fileState;
	
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

}
