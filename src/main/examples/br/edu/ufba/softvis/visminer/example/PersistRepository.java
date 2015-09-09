package br.edu.ufba.softvis.visminer.example;

import java.io.IOException;
import java.util.List;


import br.edu.ufba.softvis.visminer.constant.LanguageType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.constant.VersioningSystemType;
import br.edu.ufba.softvis.visminer.constant.WebServiceType;
import br.edu.ufba.softvis.visminer.main.VisMiner;
import br.edu.ufba.softvis.visminer.model.business.Repository;

//This example show how to persist a repository
public class PersistRepository {

	private String repositoryDescription = "Put here repository description";
	private String repositoryName = "Put here repository name";
	private String repositoryOwner = "Put here repository owner";
	private String repositoryPath = "Put here repository path";
	private VersioningSystemType repositoryType = VersioningSystemType.GIT;
	private WebServiceType repositoryServiceType = WebServiceType.GITHUB;
	private String charset = "UTF-8"; // this is the default value, does not need to be setted
	private List<MetricUid> metrics = null;
	private List<LanguageType> languages = null;
	
	public static void main(String[] args){
		VisMiner vis = new VisMiner();
		vis.setDBConfig(DatabaseConfig.getDBConfig());
		new PersistRepository().persistRepository();
	}

	public void persistRepository(){
		
		Repository repository = new Repository(0, repositoryDescription, repositoryName, repositoryPath,
				repositoryOwner, repositoryType, repositoryServiceType, "", charset);
		
		VisMiner vis = new VisMiner();
		try {
			vis.persistRepository(repository, metrics, languages);
			vis.connectWithWebRepository(repositoryPath.replace("/.git", ""));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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

	public VersioningSystemType getRepositoryType() {
		return repositoryType;
	}

	public void setRepositoryType(VersioningSystemType repositoryType) {
		this.repositoryType = repositoryType;
	}

	public String getRepositoryOwner() {
		return repositoryOwner;
	}

	public void setRepositoryOwner(String repositoryOwner) {
		this.repositoryOwner = repositoryOwner;
	}

	public WebServiceType getRepositoryServiceType() {
		return repositoryServiceType;
	}

	public void setRepositoryServiceType(WebServiceType repositoryServiceType) {
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
