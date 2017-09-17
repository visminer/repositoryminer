package org.repositoryminer.mining;

import java.io.IOException;
import java.util.List;

import org.repositoryminer.codemetric.direct.IDirectCodeMetric;
import org.repositoryminer.codesmell.direct.IDirectCodeSmell;
import org.repositoryminer.parser.IParser;
import org.repositoryminer.scm.ISCM;

public class RepositoryMiner {

	private String repositoryKey;
	private String repositoryPath;
	private String repositoryName;
	private String repositoryDescription;
	
	private ISCM scm;

	private List<IParser> parsers;
	private List<IDirectCodeMetric> directCodeMetrics;
	private List<IDirectCodeSmell> directCodeSmells;

	private List<String> references;

	public void mine() throws IOException {
		MiningProcessor processor = new MiningProcessor();
		processor.mine(this);
	}
	
	/*** Getters and Setters ***/

	public String getRepositoryPath() {
		return repositoryPath;
	}

	public String getRepositoryKey() {
		return repositoryKey;
	}

	public void setRepositoryKey(String repositoryKey) {
		this.repositoryKey = repositoryKey;
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

	public ISCM getScm() {
		return scm;
	}

	public void setScm(ISCM scm) {
		this.scm = scm;
	}

	public List<IParser> getParsers() {
		return parsers;
	}

	public void setParsers(List<IParser> parsers) {
		this.parsers = parsers;
	}

	public List<IDirectCodeMetric> getDirectCodeMetrics() {
		return directCodeMetrics;
	}

	public void setDirectCodeMetrics(List<IDirectCodeMetric> directCodeMetrics) {
		this.directCodeMetrics = directCodeMetrics;
	}

	public List<IDirectCodeSmell> getDirectCodeSmells() {
		return directCodeSmells;
	}

	public void setDirectCodeSmells(List<IDirectCodeSmell> directCodeSmells) {
		this.directCodeSmells = directCodeSmells;
	}

	public List<String> getReferences() {
		return references;
	}

	public void setReferences(List<String> references) {
		this.references = references;
	}

}