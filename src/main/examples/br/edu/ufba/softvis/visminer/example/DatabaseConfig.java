package br.edu.ufba.softvis.visminer.example;

import br.edu.ufba.softvis.visminer.config.DBConfig;

public class DatabaseConfig {

	public static DBConfig getDBConfig(){
		
		DBConfig dbConfig = new DBConfig();
		dbConfig.setDriver("com.mysql.jdbc.Driver");
		dbConfig.setUrl("jdbc:mysql://localhost/v");
		dbConfig.setUser("root");
		dbConfig.setPassword("1234");
		dbConfig.setGeneration("drop-and-create-tables");
		dbConfig.setLogging("off");
		
		return dbConfig;
		
	}
	
}
