package org.visminer.model.database;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the tree database table.
 * 
 */
@Entity
@Table(name="tree")
@NamedQuery(name="Tree.findAll", query="SELECT t FROM Tree t")
public class Tree implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id", nullable=false)
	private int id;

	@Column(name="name", nullable=false, length=255)
	private String name;
	
	@Column(name="full_name", nullable=false, length=255)
	private String fullName;

	@Column(name="type", nullable=false, length=2)
	private int type;

	//bi-directional many-to-many association to Commit
	@ManyToMany(cascade={CascadeType.PERSIST})
	@JoinTable(
		name="commit_has_tree"
		, joinColumns={
			@JoinColumn(name="tree_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="commit_id")
			}
		)
	private List<Commit> commits;

	//bi-directional many-to-one association to Repository
	@ManyToOne
	@JoinColumn(name="repository_id", nullable=false)
	private Repository repository;

	public Tree() {
	}

	public Tree(String fullName, String name, int type) {
		super();
		this.fullName = fullName;
		this.name = name;
		this.type = type;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<Commit> getCommits() {
		return this.commits;
	}

	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}

	public Repository getRepository() {
		return this.repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

}