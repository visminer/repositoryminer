package org.visminer.model.database;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the file database table.
 * 
 */
@Entity
@Table(name="file")
@NamedQuery(name="File.findAll", query="SELECT f FROM File f")
public class File implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private int id;

	@Column(name="path", length=1024, nullable=false)
	private String path;

	@Column(name="sha", length=40, nullable=false, unique=true)
	private String sha;

	//bi-directional many-to-one association to FileXCommit
	@OneToMany(mappedBy="file", fetch=FetchType.LAZY)
	private List<FileXCommit> fileXCommits;

	public File() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSha() {
		return this.sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public List<FileXCommit> getFileXCommits() {
		return this.fileXCommits;
	}

	public void setFileXCommits(List<FileXCommit> fileXCommits) {
		this.fileXCommits = fileXCommits;
	}

	public FileXCommit addFileXCommit(FileXCommit fileXCommit) {
		getFileXCommits().add(fileXCommit);
		fileXCommit.setFile(this);

		return fileXCommit;
	}

	public FileXCommit removeFileXCommit(FileXCommit fileXCommit) {
		getFileXCommits().remove(fileXCommit);
		fileXCommit.setFile(null);

		return fileXCommit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sha == null) ? 0 : sha.hashCode());
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
		if (sha == null) {
			if (other.sha != null)
				return false;
		} else if (!sha.equals(other.sha))
			return false;
		return true;
	}

}