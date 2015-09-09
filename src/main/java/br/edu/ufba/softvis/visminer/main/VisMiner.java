package br.edu.ufba.softvis.visminer.main;

import java.io.IOException;
import java.util.List;

import br.edu.ufba.softvis.visminer.analyzer.MilestoneAndIssueAnalyzer;
import br.edu.ufba.softvis.visminer.analyzer.RepositoryAnalyzer;
import br.edu.ufba.softvis.visminer.config.DBConfig;
import br.edu.ufba.softvis.visminer.config.MetricConfig;
import br.edu.ufba.softvis.visminer.constant.LanguageType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.persistence.Database;
import br.edu.ufba.softvis.visminer.utility.PropertyReader;

/**
 * @version 0.9
 * Provides core operations.
 */
public class VisMiner {

	/**
	 * @param propertiesPath
	 * @return Returns true if database was configured correctly or false otherwise.
	 * Configures database connection through a properties file.
	 */
	public boolean setDBConfig(String propertiesPath){
		PropertyReader propReader = new PropertyReader(propertiesPath);
		DBConfig config = new DBConfig(propReader);
		return setDBConfig(config);
	}
	
	/**
	 * @param config
	 * @return Returns true if database was configured correctly or false otherwise.
	 * Configures database connection through a bean.
	 */
	public boolean setDBConfig(DBConfig config){
		Database.getInstance().open(config.toMap());
		MetricConfig.setMetricsClassPath();
		return Database.getInstance().isOpen();
	}
	
	/**
	 * @param repository
	 * @param metrics
	 * Analyzes and persists repository information in database and defines what metrics will be calculated for each repository state.
	 * Set "metrics" as null if you don't want to calculate any metric.
	 * @throws IOException 
	 */
	public void persistRepository(Repository repository, List<MetricUid> metrics, List<LanguageType> languages) throws IOException{
		//languages will be used
		RepositoryAnalyzer analyzer = new RepositoryAnalyzer();
		analyzer.persist(repository, metrics, languages);
	}
	
	/**
	 * @param repositoryPath
	 * @param metrics
	 * Analyzes and persists new repository informations in database and defines what metrics will be calculated only for new repository states.
	 * Set "metrics" as null if you don't want to calculate any metric.
	 */
	public void refreshRepository(String repositoryPath, List<MetricUid> metrics, List<LanguageType> languages){
		/**TODO
		 * finalizar os  analyzers restantes
		 */
	}	
	
	/**
	 * @param repositoryPath
	 * @param metrics
	 * Does not analyze the repository, only calculates a list of metrics from beginning of the repository.
	 * @throws IOException 
	 */
	public void calculateMetrics(String repositoryPath, List<MetricUid> metrics) throws IOException{
		RepositoryAnalyzer repositoryAnalyzer = new RepositoryAnalyzer();
		repositoryAnalyzer.recalculateMetrics(repositoryPath, metrics);
	}
	
	/**
	 * 
	 * @param login
	 * @param password
	 * @param repositoryPath
	 * Synchronizes local repository with web-based repository service using user and password. 
	 */
	public void connectWithWebRepository(String login, String password, String repositoryPath){
		MilestoneAndIssueAnalyzer analyzer = new MilestoneAndIssueAnalyzer();
		analyzer.analyze(repositoryPath, login, password);
	}
	
	/**
	 * @param token
	 * @param repositoryPath
	 * Synchronizes local repository with web-based repository service using token. 
	 */
	public void connectWithWebRepository(String token, String repositoryPath){
		MilestoneAndIssueAnalyzer analyzer = new MilestoneAndIssueAnalyzer();
		analyzer.analyze(repositoryPath, token);
	}
	
	/**
	 * @param token
	 * @param repositoryPath
	 * Synchronizes local repository with web-based repository service without make login,
	 * this type connection has limitations.
	 */
	public void connectWithWebRepository(String repositoryPath){
		MilestoneAndIssueAnalyzer analyzer = new MilestoneAndIssueAnalyzer();
		analyzer.analyze(repositoryPath);
	}
	
}