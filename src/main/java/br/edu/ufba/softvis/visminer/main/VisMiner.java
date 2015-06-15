package br.edu.ufba.softvis.visminer.main;

import java.util.List;

import br.edu.ufba.softvis.visminer.analyzer.RepositoryAnalyzer;
import br.edu.ufba.softvis.visminer.config.DBConfig;
import br.edu.ufba.softvis.visminer.config.MetricConfig;
import br.edu.ufba.softvis.visminer.constant.MetricId;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.persistence.Database;
import br.edu.ufba.softvis.visminer.utility.PropertyReader;

public class VisMiner {

	public VisMiner(){}
	
	public boolean setDBConfig(String propertiesPath){
		PropertyReader propReader = new PropertyReader(propertiesPath);
		DBConfig config = new DBConfig(propReader);
		return setDBConfig(config);
	}
	
	public boolean setDBConfig(DBConfig config){
		Database.getInstance().open(config.toMap());
		MetricConfig.setMetricsClassPath();
		return Database.getInstance().isOpen();
	}
	
	public void persistRepository(Repository repository, List<MetricId> metrics){
		RepositoryAnalyzer analyzer = new RepositoryAnalyzer();
		analyzer.persist(repository, metrics);
	}
	
	public void refreshRepository(String repositoryPath, List<MetricId> metrics){
		/**TODO
		 * finalizar os  analyzers restantes
		 */
		RepositoryAnalyzer analyzer = new RepositoryAnalyzer();
		analyzer.update(repositoryPath);
	}	
	
	public void calculateMetrics(List<MetricId> metrics){
		/**TODO
		 * fazer essa parte usando uma variação do codigo de persistencia do repositorio
		 */
	}
	
}