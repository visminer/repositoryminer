package br.edu.ufba.softvis.visminer.example;

import java.util.List;

import br.edu.ufba.softvis.visminer.constant.LanguageType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.constant.RepositoryType;
import br.edu.ufba.softvis.visminer.constant.WebRepositoryType;
import br.edu.ufba.softvis.visminer.main.VisMiner;
import br.edu.ufba.softvis.visminer.model.business.Repository;

//This example show how to persist a repository
public class PersistRepository {

	private String repositoryDescription = "Test";
	private String repositoryName = "Test";
	private String repositoryPath = "/home/felipe/git/jgit-cookbook";
	private RepositoryType repositoryType = RepositoryType.GIT;
	private String repositoryRemoteUrl = "https://github.com/centic9/jgit-cookbook";
	private WebRepositoryType repositoryServiceType = WebRepositoryType.GITHUB;
	private String charset = "UTF-8";
	private List<MetricUid> metrics = null;
	private List<LanguageType> languages = null;
	
	public static void main(String[] args) {

		VisMiner vis = new VisMiner();
		vis.setDBConfig(DatabaseConfig.getDBConfig());
		new PersistRepository().persistRepository();
		
	}

	public void persistRepository(){
		
		Repository repository = new Repository(0, repositoryDescription, repositoryName, repositoryPath,
				repositoryRemoteUrl, repositoryType, repositoryServiceType, "", charset);
		
		VisMiner vis = new VisMiner();
		vis.persistRepository(repository, metrics, languages);
		
	}

	public String getRepositoryDescription() {
		return repositoryDescription;
	}

	public void setRepositoryDescription(String repositoryDescription) {
		this.repositoryDescription = repositoryDescription;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public String getRepositoryPath() {
		return repositoryPath;
	}

	public void setRepositoryPath(String repositoryPath) {
		this.repositoryPath = repositoryPath;
	}

	public RepositoryType getRepositoryType() {
		return repositoryType;
	}

	public void setRepositoryType(RepositoryType repositoryType) {
		this.repositoryType = repositoryType;
	}

	public String getRepositoryRemoteUrl() {
		return repositoryRemoteUrl;
	}

	public void setRepositoryRemoteUrl(String repositoryRemoteUrl) {
		this.repositoryRemoteUrl = repositoryRemoteUrl;
	}

	public WebRepositoryType getRepositoryServiceType() {
		return repositoryServiceType;
	}

	public void setRepositoryServiceType(WebRepositoryType repositoryServiceType) {
		this.repositoryServiceType = repositoryServiceType;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public List<MetricUid> getMetrics() {
		return metrics;
	}

	public void setMetrics(List<MetricUid> metrics) {
		this.metrics = metrics;
	}

	public List<LanguageType> getLanguages() {
		return languages;
	}

	public void setLanguages(List<LanguageType> languages) {
		this.languages = languages;
	}

}
