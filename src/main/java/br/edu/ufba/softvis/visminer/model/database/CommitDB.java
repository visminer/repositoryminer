package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * The persistent class for the commit database table.
 */
@Entity
@Table(name="commit")
@NamedQueries({
	@NamedQuery(name="CommitDB.findByTree", query="select c from TreeDB t join t.commits c where t.id = :id order by c.date")
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

	/**
	 * @param id
	 * @param date
	 * @param message
	 * @param name
	 */
	public CommitDB(int id, Date date, String message, String name) {
		super();
		this.id = id;
		this.date = date;
		this.message = message;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the committer
	 */
	public CommitterDB getCommitter() {
		return committer;
	}

	/**
	 * @param committer the committer to set
	 */
	public void setCommitter(CommitterDB committer) {
		this.committer = committer;
	}

	/**
	 * @return the trees
	 */
	public List<TreeDB> getTrees() {
		return trees;
	}

	/**
	 * @param trees the trees to set
	 */
	public void setTrees(List<TreeDB> trees) {
		this.trees = trees;
	}

	/**
	 * @return the fileXCommits
	 */
	public List<FileXCommitDB> getFileXCommits() {
		return fileXCommits;
	}

	/**
	 * @param fileXCommits the fileXCommits to set
	 */
	public void setFileXCommits(List<FileXCommitDB> fileXCommits) {
		this.fileXCommits = fileXCommits;
	}

	/**
	 * @return the issues
	 */
	public List<IssueDB> getIssues() {
		return issues;
	}

	/**
	 * @param issues the issues to set
	 */
	public void setIssues(List<IssueDB> issues) {
		this.issues = issues;
	}

	/**
	 * @return the softwareUnits
	 */
	public List<SoftwareUnitDB> getSoftwareUnits() {
		return softwareUnits;
	}

	/**
	 * @param softwareUnits the softwareUnits to set
	 */
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

}