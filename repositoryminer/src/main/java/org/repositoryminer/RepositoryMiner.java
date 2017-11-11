package org.repositoryminer;

import java.io.IOException;

import org.repositoryminer.domain.SCMType;
import org.repositoryminer.persistence.RepositoryDAO;

/**
 * The front-end class to perform the repository data extraction.
 */
public class RepositoryMiner {

	private String repositoryKey;
	private String repositoryPath;
	private String repositoryName;
	private String repositoryDescription;
	private SCMType scm;

	public void mine() throws IOException {
		RepositoryDAO repoDocHandler = new RepositoryDAO();
		if (!repoDocHandler.wasMined(repositoryKey)) {
			ExtractionProcessor processor = new ExtractionProcessor();
			processor.extract(this);
		}
		// TODO: update repository mining feature.
	}

	public RepositoryMiner() {}
	
	public RepositoryMiner(String repositoryKey, String repositoryPath, String repositoryName,
			String repositoryDescription, SCMType scm) {
		super();
		this.repositoryKey = repositoryKey;
		this.repositoryPath = repositoryPath;
		this.repositoryName = repositoryName;
		this.repositoryDescription = repositoryDescription;
		this.scm = scm;
	}

	/*** Getters and Setters ***/
	
	public String getRepositoryKey() {
		return repositoryKey;
	}

	public void setRepositoryKey(String repositoryKey) {
		this.repositoryKey = repositoryKey;
	}

	public String getRepositoryPath() {
		return repositoryPath;
	}

	public void setRepositoryPath(String repositoryPath) {
		this.repositoryPath = repositoryPath;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public String getRepositoryDescription() {
		return repositoryDescription;
	}

	public void setRepositoryDescription(String repositoryDescription) {
		this.repositoryDescription = repositoryDescription;
	}

	public SCMType getSCM() {
		return scm;
	}

	public void setSCM(SCMType scm) {
		this.scm = scm;
	}

}