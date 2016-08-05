package org.repositoryminer.persistence.model;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.scm.ReferenceType;


/**
 * This class represents the "reference" object in the database.
 * This class represents a reference.
 */
public class ReferenceDB {

	private String id;
	private String repository;
	private String name;
	private String fullName;
	private ReferenceType type;
	private List<String> commits;

	public ReferenceDB(){}
	
	public Document toDocument() {
		Document doc = new Document();
		doc.append("_id", id).append("repository", repository).append("name", name).append("full_name", fullName).
		append("type", type.toString()).append("commits", commits);
		return doc;
	}
	
	public ReferenceDB(String id, String repository, String name, String fullName, ReferenceType type) {
		super();
		this.id = id;
		this.repository = repository;
		this.name = name;
		this.fullName = fullName;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getName() {
		return name;
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

	public ReferenceType getType() {
		return type;
	}

	public void setType(ReferenceType type) {
		this.type = type;
	}

	public List<String> getCommits() {
		return commits;
	}

	public void setCommits(List<String> commits) {
		this.commits = commits;
	}

}