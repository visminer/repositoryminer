package br.edu.ufba.softvis.visminer.model;

import java.util.List;

import org.bson.Document;

public class Repository {
	private String uid;
	private String description;
	private String name;
	private String path;
	private String owner;

	private List<Committer> committers;
	private List<Commit> commits;
	private List<Tree> trees;

	public Repository() {
	}

	public Repository(String uid, String description, String name, String path, String owner) {
		super();
		this.uid = uid;
		this.description = description;
		this.name = name;
		this.path = path;
		this.owner = owner;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid
	 *            the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the commits
	 */
	public List<Commit> getCommits() {
		return commits;
	}

	/**
	 * @param commits
	 *            the commits to set
	 */
	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}

	/**
	 * @return the committers
	 */
	public List<Committer> getCommitters() {
		return committers;
	}

	/**
	 * @param committers
	 *            the committers to set
	 */
	public void setCommitters(List<Committer> committers) {
		this.committers = committers;
	}

	/**
	 * @return the trees
	 */
	public List<Tree> getTrees() {
		return trees;
	}

	/**
	 * @param trees
	 *            the trees to set
	 */
	public void setTrees(List<Tree> trees) {
		this.trees = trees;
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		return uid.hashCode();
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Repository other = (Repository) obj;
		return uid.equals(other.getUid());
	}

	/**
	 * @return mongo's document format of repository
	 */
	public Document toDocument() {
		Document doc = new Document("uid", getUid()).append("description", getDescription()).append("name", getName())
				.append("path", getPath()).append("owner", getOwner());

		return doc;
	}

}