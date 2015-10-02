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
	
	/*
	 * repositoryPath must be changed to the correct path on you environment
	 * */
	//private String repositoryPath = "C:\\renato\\dev\\pagseguro-test"; 
	private String repositoryPath = "C:/renato/dev/Visminer-Test"; 
	
	private static VisminerTest vt;

	private VisminerTest() {
		visminer = new VisMiner();
		configParameters();
		repoRetriever = new RepositoryRetriever();
		if (!isRepositoryProcessed()) {
			startRepository();
		}

		repository = repoRetriever.retrieveByPath(repositoryPath);
		//printCommitters(listCommitters());

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
		dbConfig.setUrl("jdbc:mysql://localhost/visminer-test");
		dbConfig.setUser("root");
		dbConfig.setPassword("admin");
		dbConfig.setGeneration("create-tables"); //only creates tables if they do not exist
		//dbConfig.setGeneration("drop-and-create-tables"); //drop and create the tables anyway
		dbConfig.setLogging("off");
		visminer.setDBConfig(dbConfig);

	}

	private void startRepository() {

		Repository repository = new Repository();
		repository.setDescription("VisMiner Repository");
		repository.setName("visminer");
		repository.setPath(repositoryPath);
		repository.setType(SCMType.GIT);

		// repository.setCharset("UTF-8"); N„o È obrigatÛrio

		MetricUid[] metrics = { MetricUid.SLOC, MetricUid.CC, MetricUid.NOCAI, 
				MetricUid.WMC, MetricUid.TCC };
		LanguageType[] langs = { LanguageType.JAVA };
		try {
			// Arrays.asList(metrics)
			visminer.persistRepository(repository, Arrays.asList(metrics), Arrays.asList(langs));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private boolean isRepositoryProcessed() {
		return visminer.checkRepository(repositoryPath);
	}
	
	public String removeRepositoryPathNameFromFileName(String fileName){
		String preffix = repositoryPath;
			
		if (fileName.startsWith(repositoryPath)){
			if (!preffix.endsWith("/")) preffix += "/";
			fileName = fileName.substring(preffix.length());
			
		}
		return fileName;
	}

}
