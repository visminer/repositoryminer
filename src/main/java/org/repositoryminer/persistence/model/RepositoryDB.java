package org.repositoryminer.persistence.model;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.scm.SCMType;

/**
 * This class represents the "repository" object in the database. This class
 * represents a repository.
 */
public class RepositoryDB {

	private String id;
	private String name;
	private String description;
	private String path;
	private SCMType scm;
	private List<ContributorDB> contributors;

	public RepositoryDB() {
	}

	public RepositoryDB(org.repositoryminer.mining.RepositoryMiner repo) {
		super();
		this.name = repo.getName();
		this.description = repo.getDescription();
		this.path = repo.getPath();
		this.scm = repo.getScm();
	}

	public Document toDocument() {
		Document doc = new Document();
		doc.append("_id", id).append("name", name).append("description", description).append("path", path)
				.append("scm", scm.toString()).append("contributors", ContributorDB.toDocumentList(contributors));
		return doc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public SCMType getScm() {
		return scm;
	}

	public void setScm(SCMType scm) {
		this.scm = scm;
	}

	public List<ContributorDB> getContributors() {
		return contributors;
	}

	public void setContributors(List<ContributorDB> contributors) {
		this.contributors = contributors;
	}

}