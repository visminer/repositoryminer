package org.visminer.model.database;

import java.io.Serializable;

import javax.persistence.*;

import org.visminer.utility.StringDigest;

import java.util.List;


/**
 * The persistent class for the repository database table.
 * 
 */
@Entity
@Table(name="repository")
@NamedQuery(name="Repository.findAll", query="SELECT r FROM Repository r")
public class Repository implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private int id;
	
	@Column(name="name", length=255, nullable=false)
	private String name;

	@Column(name="path", length=1024, nullable=false)
	private String path;

	@Column(name="remote_name", length=255, nullable=true)
	private String remoteName;

	@Column(name="remote_owner", length=255, nullable=true)
	private String remoteOwner;

	@Column(name="remote_service", length=2, nullable=true)
	private int remoteService;

	@Column(name="sha", length=40, nullable=false, unique=true)
	private String sha;

	@Column(name="type", length=2, nullable=false)
	private int type;

	//bi-directional many-to-many association to Committer
	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(
		name="committer_has_repository"
		, joinColumns={
			@JoinColumn(name="repository_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="committer_id")
			}
		)
	private List<Committer> committers;

	//bi-directional many-to-one association to Tree
	@OneToMany(mappedBy="repository")
	private List<Tree> trees;

	public Repository() {
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

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRemoteName() {
		return this.remoteName;
	}

	public void setRemoteName(String remoteName) {
		this.remoteName = remoteName;
	}

	public String getRemoteOwner() {
		return this.remoteOwner;
	}

	public void setRemoteOwner(String remoteOwner) {
		this.remoteOwner = remoteOwner;
	}

	public int getRemoteService() {
		return this.remoteService;
	}

	public void setRemoteService(int remoteService) {
		this.remoteService = remoteService;
	}

	public String getSha() {
		return this.sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<Committer> getCommitters() {
		return this.committers;
	}

	public void setCommitters(List<Committer> committers) {
		this.committers = committers;
	}

	public List<Tree> getTrees() {
		return this.trees;
	}

	public void setTrees(List<Tree> trees) {
		this.trees = trees;
	}

	public Tree addTree(Tree tree) {
		getTrees().add(tree);
		tree.setRepository(this);

		return tree;
	}

	public Tree removeTree(Tree tree) {
		getTrees().remove(tree);
		tree.setRepository(null);

		return tree;
	}
	
	public void getValuesOf(org.visminer.model.business.Repository repository){
		
		this.name = repository.getName();
		this.path = repository.getPath();
		this.remoteName = repository.getRemote_name();
		this.remoteOwner = repository.getRemote_owner();
		this.remoteService = repository.getRemoteType();
		this.type = repository.getType();
		this.sha = StringDigest.sha1(this.path);
		
	}

}