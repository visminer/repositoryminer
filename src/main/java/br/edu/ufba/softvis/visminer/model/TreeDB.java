package br.edu.ufba.softvis.visminer.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the tree database table.
 * 
 */
@Entity
@Table(name="tree")
@NamedQuery(name="TreeDB.findAll", query="SELECT t FROM TreeDB t")
public class TreeDB implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TREE_ID_GENERATOR", sequenceName="TREE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TREE_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="full_name", nullable=false, length=255)
	private String fullName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_update", nullable=false)
	private Date lastUpdate;

	@Column(nullable=false, length=100)
	private String name;

	@Column(nullable=false)
	private int type;

	//bi-directional many-to-many association to CommitDB
	@ManyToMany
	@JoinTable(
		name="commit_reference_tree"
		, joinColumns={
			@JoinColumn(name="tree_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="commit_id", nullable=false)
			}
		)
	private List<CommitDB> commits;

	//bi-directional many-to-one association to RepositoryDB
	@ManyToOne
	@JoinColumn(name="repository_id", nullable=false)
	private RepositoryDB repository;

	public TreeDB() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<CommitDB> getCommits() {
		return this.commits;
	}

	public void setCommits(List<CommitDB> commits) {
		this.commits = commits;
	}

	public RepositoryDB getRepository() {
		return this.repository;
	}

	public void setRepository(RepositoryDB repository) {
		this.repository = repository;
	}

}