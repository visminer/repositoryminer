package br.edu.ufba.softvis.visminer.main;

import java.io.IOException;
import java.util.List;

import br.edu.ufba.softvis.visminer.analyzer.RepositoryAnalyzer;
import br.edu.ufba.softvis.visminer.antipattern.IAntiPattern;
import br.edu.ufba.softvis.visminer.config.DBConfig;
import br.edu.ufba.softvis.visminer.constant.LanguageType;
import br.edu.ufba.softvis.visminer.constant.SCMType;
import br.edu.ufba.softvis.visminer.metric.IMetric;
import br.edu.ufba.softvis.visminer.model.Repository;
import br.edu.ufba.softvis.visminer.persistence.Database;
import br.edu.ufba.softvis.visminer.utility.PropertyReader;

/**
 * Provides core operations.
 */
public class VisMiner {

	/**
	 * Configures database connection through a properties file.
	 * 
	 * @param propertiesPath
	 */
	public void setDBConfig(String propertiesPath) {
		PropertyReader propReader = new PropertyReader(propertiesPath);
		DBConfig config = new DBConfig(propReader);
		setDBConfig(config);
	}

	/**
	 * Configures database connection through a bean.
	 * 
	 * @param config
	 */
	public void setDBConfig(DBConfig config) {
		Database.getInstance().prepare(config);
	}

	/**
	 * @param repository
	 * @param metrics
	 *            Analyzes and persists repository information in database and
	 *            defines what metrics will be calculated for each repository
	 *            state. Set "metrics" as null if you don't want to calculate
	 *            any metric.
	 * @throws IOException
	 */
	public void mineRepository(Repository repository, SCMType type, List<IMetric> metrics, List<IAntiPattern> antiPatterns,
			List<LanguageType> languages) throws IOException {
		// languages will be used
		RepositoryAnalyzer analyzer = new RepositoryAnalyzer();
		analyzer.persist(repository, type, metrics, antiPatterns, languages);
	}

}