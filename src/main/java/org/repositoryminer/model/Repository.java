package org.repositoryminer.model;

import java.util.List;

import org.bson.Document;

/**
 * This class represents the "repository" object in the database. This class
 * represents a repository.
 */
public class Repository {

	private String id;
	private String name;
	private String description;
	private String path;
	private SCMType scm;
	private List<Contributor> contributors;

	public Repository() {
	}

	public Repository(org.repositoryminer.scm.SCMRepository repo) {
		super();
		this.name = repo.getName();
		this.description = repo.getDescription();
		this.path = repo.getPath();
		this.scm = repo.getScm();
	}

	public Document toDocument() {
		Document doc = new Document();
		doc.append("_id", id).append("name", name).append("description", description).append("path", path)
				.append("scm", scm.toString()).append("contributors", Contributor.toDocumentList(contributors));
		return doc;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the scm
	 */
	public SCMType getScm() {
		return scm;
	}

	/**
	 * @param scm
	 *            the scm to set
	 */
	public void setScm(SCMType scm) {
		this.scm = scm;
	}

	/**
	 * @return the contributors
	 */
	public List<Contributor> getContributors() {
		return contributors;
	}

	/**
	 * @param contributors
	 *            the contributors to set
	 */
	public void setContributors(List<Contributor> contributors) {
		this.contributors = contributors;
	}

}