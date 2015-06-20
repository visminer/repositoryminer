package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;
import javax.persistence.*;


/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * The persistent class for the file_x_commit database table.
 */
@Entity
@Table(name="file_x_commit")
@NamedQuery(name="FileXCommitDB.findByCommit", query="SELECT fxc FROM FileXCommitDB fxc where fxc.id.commitId = :commitId")
public class FileXCommitDB implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FileXCommitPK id;

	@Column(name="lines_added")
	private int linesAdded;

	@Column(name="lines_removed")
	private int linesRemoved;

	@Column(nullable=false)
	private boolean removed;

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

	/**
	 * @param id
	 * @param linesAdded
	 * @param linesRemoved
	 * @param removed
	 */
	public FileXCommitDB(FileXCommitPK id, int linesAdded, int linesRemoved,
			boolean removed) {
		super();
		this.id = id;
		this.linesAdded = linesAdded;
		this.linesRemoved = linesRemoved;
		this.removed = removed;
	}

	/**
	 * @return the id
	 */
	public FileXCommitPK getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(FileXCommitPK id) {
		this.id = id;
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
	 * @return the removed
	 */
	public boolean isRemoved() {
		return removed;
	}

	/**
	 * @param removed the removed to set
	 */
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	/**
	 * @return the commit
	 */
	public CommitDB getCommit() {
		return commit;
	}

	/**
	 * @param commit the commit to set
	 */
	public void setCommit(CommitDB commit) {
		this.commit = commit;
	}

	/**
	 * @return the file
	 */
	public FileDB getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(FileDB file) {
		this.file = file;
	}

}