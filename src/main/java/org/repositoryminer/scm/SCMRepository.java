package org.repositoryminer.scm;

import org.repositoryminer.model.SCMType;

public class SCMRepository {

	private String path;
	private String name;
	private String description;
	private SCMType scm;
	
	public SCMRepository(){}

	public SCMRepository(String path, String name, String description, SCMType scm) {
		super();
		this.path = path;
		this.name = name;
		this.description = description;
		this.scm = scm;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the scm
	 */
	public SCMType getScm() {
		return scm;
	}

	/**
	 * @param scm the scm to set
	 */
	public void setScm(SCMType scm) {
		this.scm = scm;
	}

}