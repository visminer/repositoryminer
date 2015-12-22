package br.edu.ufba.softvis.visminer.model;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.constant.ChangeType;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9 User friendly file bean class. This class will be used for user
 *          interface.
 */
public class File {

	private String uid;
	private String path;
	private FileState fileState;

	public File() {
		this.fileState = new FileState();
	}

	public File(String uid, String path) {
		super();

		this.uid = uid;
		this.path = path;
		this.fileState = new FileState();
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public FileState getFileState() {
		return fileState;
	}

	public void setFileState(FileState fileState) {
		this.fileState = fileState;
	}

	public ChangeType getChangeType() {
		return fileState.getChange();
	}

	public void setChangeType(ChangeType changeType) {
		fileState.setChange(changeType);
	}

	public int getLinesAdded() {
		return fileState.getLinesAdded();
	}

	public void setLinesAdded(int linesAdded) {
		fileState.setLinesAdded(linesAdded);
	}

	public int getLinesRemoved() {
		return fileState.getLinesRemoved();
	}

	public void setLinesRemoved(int linesRemoved) {
		fileState.setLinesRemoved(linesRemoved);
	}

	public Document toDocument() {
		Document doc = new Document("uid", uid).append("path", path)
				.append("state", fileState.getChange().name())
				.append("linesAdded", fileState.getLinesAdded())
				.append("linesRemoved", fileState.getLinesRemoved());

		return doc;
	}

	@Override
	public int hashCode() {
		return uid.hashCode();
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
		return (uid.equals(other.getUid()));
	}

}