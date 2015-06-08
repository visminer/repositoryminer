package br.edu.ufba.softvis.visminer.main;

import br.edu.ufba.softvis.visminer.analyzer.RepositoryAnalyzer;
import br.edu.ufba.softvis.visminer.config.DBConfig;
import br.edu.ufba.softvis.visminer.config.MetricConfig;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.persistence.Database;

public class VisMiner {

	public VisMiner(){}
	
	public void setDBConfig(String propertiesPath){
	}
	
	public boolean setDBConfig(DBConfig config){
		Database.getInstance().open(config.toMap());
		return Database.getInstance().isOpen();
	}
	
	public void init(){
		MetricConfig.setMetricsClassPath(null);
	}

	public void persistRepository(Repository repository){
		RepositoryAnalyzer analyzer = new RepositoryAnalyzer();
		analyzer.persist(repository);
	}
	
	public void refreshRepository(String repositoryPath){
		RepositoryAnalyzer analyzer = new RepositoryAnalyzer();
		analyzer.update(repositoryPath);
	}
	
}