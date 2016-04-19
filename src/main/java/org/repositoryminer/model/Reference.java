package org.repositoryminer.model;

import java.util.List;

import org.bson.Document;


/**
 * This class represents the "reference" object in the database.
 * This class represents a reference.
 */
public class Reference {

	private String id;
	private String repository;
	private String name;
	private String fullName;
	private ReferenceType type;
	private List<String> commits;

	public Reference(){}
	
	public Document toDocument() {
		Document doc = new Document();
		doc.append("_id", id).append("repository", repository).append("name", name).append("full_name", fullName).
		append("type", type.getId()).append("commits", commits);
		return doc;
	}
	
	public Reference(String id, String repository, String name, String fullName, ReferenceType type) {
		super();
		this.id = id;
		this.repository = repository;
		this.name = name;
		this.fullName = fullName;
		this.type = type;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the repository
	 */
	public String getRepository() {
		return repository;
	}

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(String repository) {
		this.repository = repository;
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
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the type
	 */
	public ReferenceType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ReferenceType type) {
		this.type = type;
	}

	/**
	 * @return the commits
	 */
	public List<String> getCommits() {
		return commits;
	}

	/**
	 * @param commits the commits to set
	 */
	public void setCommits(List<String> commits) {
		this.commits = commits;
	}

}