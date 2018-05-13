package org.repositoryminer;

import java.io.IOException;

import org.repositoryminer.domain.SCMType;
import org.repositoryminer.persistence.RepositoryDAO;

/**
 * The front-end class to perform the repository data extraction.
 */
public class RepositoryMiner {

	private String key;
	private String path;
	private String name;
	private String description;
	private SCMType scm;

	/**
	 * Starts the SCM data extraction process. If a repository was analyzed before,
	 * its informations will be updated.
	 * 
	 * @throws IOException
	 */
	public void mine() throws IOException {
		RepositoryDAO repoDocHandler = new RepositoryDAO();
		if (!repoDocHandler.wasMined(key)) {
			ExtractionProcessor.extract(this);
		}
		// TODO: update repository mining feature.
	}

	public RepositoryMiner(String key) {
		this.key = key;
	}

	public RepositoryMiner(String key, String path, String name,
			String description, SCMType scm) {
		this.key = key;
		this.path = path;
		this.name = name;
		this.description = description;
		this.scm = scm;
	}

	/*** Getters and Setters ***/

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

	public SCMType getSCM() {
		return scm;
	}

	public void setSCM(SCMType scm) {
		this.scm = scm;
	}

}
