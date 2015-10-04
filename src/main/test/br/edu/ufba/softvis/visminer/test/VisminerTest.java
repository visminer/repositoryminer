package br.edu.ufba.softvis.visminer.test;
import java.io.IOException;
import java.util.Arrays;

import br.edu.ufba.softvis.visminer.config.DBConfig;
import br.edu.ufba.softvis.visminer.constant.LanguageType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.constant.SCMType;
import br.edu.ufba.softvis.visminer.main.VisMiner;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.retriever.RepositoryRetriever;

public class VisminerTest {
	
	private VisMiner visminer;
	private Repository repository;
	private RepositoryRetriever repoRetriever;
	
	private String repositoryPath = "C:\\Users\\felipe\\test-felipe\\Visminer-Test"; 
	private static VisminerTest vt;

	private VisminerTest() {
		
		visminer = new VisMiner();
		configParameters();
		repoRetriever = new RepositoryRetriever();
		
		if (!isRepositoryProcessed()) {
			startRepository();
		}

		repository = repoRetriever.retrieveByPath(repositoryPath);

	}

	public static VisminerTest getInstance() {

		if (vt == null) {
			vt = new VisminerTest();
		}
		return vt;
		
	}

	public VisMiner getVisminer() {
		return visminer;
	}

	public Repository getRepository() {
		return repository;
	}

	public RepositoryRetriever getRepositoryRetriever() {
		return repoRetriever;
	}

	/*
	 * change the parameters accordingly
	 * */
	private void configParameters() {

		DBConfig dbConfig = new DBConfig();
		dbConfig.setDriver("com.mysql.jdbc.Driver");
		dbConfig.setUrl("jdbc:mysql://localhost/visminer");
		dbConfig.setUser("root");
		dbConfig.setPassword("1234");
		dbConfig.setGeneration("none");
		dbConfig.setLogging("off");
		visminer.setDBConfig(dbConfig);

	}

	private void startRepository() {

		Repository repository = new Repository();
		repository.setDescription("Repositório do Visminer");
		repository.setOwner("visminer");
		repository.setName("Visminer");
		repository.setPath(repositoryPath);
		repository.setType(SCMType.GIT);

		MetricUid[] metrics = {MetricUid.ATFD, MetricUid.CC, MetricUid.NOCAI, MetricUid.SLOC, MetricUid.TCC, MetricUid.WMC};
		LanguageType[] langs = {LanguageType.JAVA};
		
		try {
			visminer.persistRepository(repository, Arrays.asList(metrics), Arrays.asList(langs));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private boolean isRepositoryProcessed() {
		return visminer.checkRepository(repositoryPath);
	}
	
}
