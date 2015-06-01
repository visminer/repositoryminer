package br.edu.ufba.softvis.visminer.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the file_x_commit database table.
 * 
 */
@Entity
@Table(name="file_x_commit")
@NamedQuery(name="FileXCommitDB.findAll", query="SELECT f FROM FileXCommitDB f")
public class FileXCommitDB implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FileXCommitPK id;

	@Column(name="lines_added")
	private int linesAdded;

	@Column(name="lines_removed")
	private int linesRemoved;

	@Column(nullable=false)
	private byte removed;

	//bi-directional many-to-one association to CommitDB
	@ManyToOne
	@JoinColumn(name="commit_id", nullable=false, insertable=false, updatable=false)
	private CommitDB commit;

	//bi-directional many-to-one association to FileDB
	@ManyToOne
	@JoinColumn(name="file_id", nullable=false, insertable=false, updatable=false)
	private FileDB file;

	public FileXCommitDB() {
	}

	public FileXCommitPK getId() {
		return this.id;
	}

	public void setId(FileXCommitPK id) {
		this.id = id;
	}

	public int getLinesAdded() {
		return this.linesAdded;
	}

	public void setLinesAdded(int linesAdded) {
		this.linesAdded = linesAdded;
	}

	public int getLinesRemoved() {
		return this.linesRemoved;
	}

	public void setLinesRemoved(int linesRemoved) {
		this.linesRemoved = linesRemoved;
	}

	public byte getRemoved() {
		return this.removed;
	}

	public void setRemoved(byte removed) {
		this.removed = removed;
	}

	public CommitDB getCommit() {
		return this.commit;
	}

	public void setCommit(CommitDB commit) {
		this.commit = commit;
	}

	public FileDB getFile() {
		return this.file;
	}

	public void setFile(FileDB file) {
		this.file = file;
	}

}