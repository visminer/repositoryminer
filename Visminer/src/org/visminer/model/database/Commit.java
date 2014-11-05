package org.visminer.model.database;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the commit database table.
 * 
 */
@Entity
@Table(name="commit")
@NamedQuery(name="Commit.findAll", query="SELECT c FROM Commit c")
public class Commit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date", nullable=false)
	private Date date;

	@Lob
	@Column(name="message", nullable=false)
	private String message;

	@Column(name="name", length=40, nullable=false, unique=true)
	private String name;

	//bi-directional many-to-one association to Committer
	@ManyToOne
	@JoinColumn(nullable=false, name="committer_id")
	private Committer committer;

	//bi-directional many-to-many association to Tree
	@ManyToMany(mappedBy="commits")
	private List<Tree> trees;

	//bi-directional many-to-one association to FileXCommit
	@OneToMany(mappedBy="commit", fetch=FetchType.LAZY)
	private List<FileXCommit> fileXCommits;

	public Commit() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Committer getCommitter() {
		return this.committer;
	}

	public void setCommitter(Committer committer) {
		this.committer = committer;
	}

	public List<Tree> getTrees() {
		return this.trees;
	}

	public void setTrees(List<Tree> trees) {
		this.trees = trees;
	}

	public List<FileXCommit> getFileXCommits() {
		return this.fileXCommits;
	}

	public void setFileXCommits(List<FileXCommit> fileXCommits) {
		this.fileXCommits = fileXCommits;
	}

	public FileXCommit addFileXCommit(FileXCommit fileXCommit) {
		getFileXCommits().add(fileXCommit);
		fileXCommit.setCommit(this);

		return fileXCommit;
	}

	public FileXCommit removeFileXCommit(FileXCommit fileXCommit) {
		getFileXCommits().remove(fileXCommit);
		fileXCommit.setCommit(null);

		return fileXCommit;
	}

	public void addTree(Tree tree) {
		getTrees().add(tree);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Commit other = (Commit) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}