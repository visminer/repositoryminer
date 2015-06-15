package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;

import javax.persistence.*;

import br.edu.ufba.softvis.visminer.model.bean.Commit;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the commit database table.
 * 
 */
@Entity
@Table(name="commit")
@NamedQueries({
	@NamedQuery(name="CommitDB.findByTree", query="select c from TreeDB t join t.commits c where t.id = :id")
})

public class CommitDB implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="COMMIT_ID_GENERATOR", sequenceName="COMMIT_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMMIT_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date date;

	@Lob
	private String message;

	@Column(nullable=false, length=40)
	private String name;

	//bi-directional many-to-one association to CommitterDB
	@ManyToOne
	@JoinColumn(name="committer_id", nullable=false)
	private CommitterDB committer;

	//bi-directional many-to-many association to TreeDB
	@ManyToMany(mappedBy="commits")
	private List<TreeDB> trees;

	//bi-directional many-to-one association to FileXCommitDB
	@OneToMany(mappedBy="commit")
	private List<FileXCommitDB> fileXCommits;

	//bi-directional many-to-many association to IssueDB
	@ManyToMany(mappedBy="commits")
	private List<IssueDB> issues;

	//bi-directional many-to-many association to SoftwareUnitDB
	@ManyToMany
	@JoinTable(
		name="software_unit_x_commit"
		, joinColumns={
			@JoinColumn(name="commit_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="software_unit_id", nullable=false)
			}
		)
	private List<SoftwareUnitDB> softwareUnits;

	public CommitDB() {
	}

	public CommitDB(br.edu.ufba.softvis.visminer.model.bean.Commit commit){
		this.date = commit.getDate();
		this.message = commit.getMessage();
		this.name = commit.getName();
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

	public CommitterDB getCommitter() {
		return this.committer;
	}

	public void setCommitter(CommitterDB committer) {
		this.committer = committer;
	}

	public List<TreeDB> getTrees() {
		return this.trees;
	}

	public void setTrees(List<TreeDB> trees) {
		this.trees = trees;
	}

	public List<FileXCommitDB> getFileXCommits() {
		return this.fileXCommits;
	}

	public void setFileXCommits(List<FileXCommitDB> fileXCommits) {
		this.fileXCommits = fileXCommits;
	}

	public FileXCommitDB addFileXCommit(FileXCommitDB fileXCommit) {
		getFileXCommits().add(fileXCommit);
		fileXCommit.setCommit(this);

		return fileXCommit;
	}

	public FileXCommitDB removeFileXCommit(FileXCommitDB fileXCommit) {
		getFileXCommits().remove(fileXCommit);
		fileXCommit.setCommit(null);

		return fileXCommit;
	}

	public List<IssueDB> getIssues() {
		return this.issues;
	}

	public void setIssues(List<IssueDB> issues) {
		this.issues = issues;
	}

	public List<SoftwareUnitDB> getSoftwareUnits() {
		return this.softwareUnits;
	}

	public void setSoftwareUnits(List<SoftwareUnitDB> softwareUnits) {
		this.softwareUnits = softwareUnits;
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
		CommitDB other = (CommitDB) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public void clean() {
		this.date = null;
		this.committer = null;
		this.message = null;
	}

	public Commit retrieveBean() {
		
		return new Commit(id, date, message, name);
		
	}

}