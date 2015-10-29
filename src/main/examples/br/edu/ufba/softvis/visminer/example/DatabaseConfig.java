package br.edu.ufba.softvis.visminer.example;

import br.edu.ufba.softvis.visminer.config.DBConfig;

public class DatabaseConfig {

	public static DBConfig getDBConfig(){
		
		DBConfig dbConfig = new DBConfig();
		dbConfig.setDriver("com.mysql.jdbc.Driver");
		dbConfig.setUrl("jdbc:mysql://localhost/vismine");
		dbConfig.setUser("root");
		dbConfig.setPassword("gv060793");
		dbConfig.setGeneration("drop-and-create-tables");
		dbConfig.setLogging("off");
		
		return dbConfig;
		
	}
	
}
