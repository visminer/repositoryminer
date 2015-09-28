package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;
import javax.persistence.*;

import br.edu.ufba.softvis.visminer.constant.ChangeType;


/**
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
	private int change;

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
			ChangeType change) {
		super();
		this.id = id;
		this.linesAdded = linesAdded;
		this.linesRemoved = linesRemoved;
		this.change = change != null ? change.getId() : 0;
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
	 * @return the change
	 */
	public ChangeType getChange() {
		return ChangeType.parse(change);
	}

	/**
	 * @param change the change to set
	 */
	public void setChange(ChangeType change) {
		this.change = change != null ? change.getId() : 0;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		FileXCommitDB other = (FileXCommitDB) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}