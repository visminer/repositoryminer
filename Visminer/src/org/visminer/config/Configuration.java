package org.visminer.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.visminer.persistence.Connection;

public class Configuration {

	public static void configure(String path){
		
		File f = new File(path);

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			Properties props = new Properties();
			props.load(fis);
			setCfgValues(props);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	private static void setCfgValues(Properties props){
		
		Map<String, String> dbCfg = new HashMap<String, String>();
		dbCfg.put(ConfigProperties.JDBC_DRIVER, props.getProperty("jdbc.driver"));
		dbCfg.put(ConfigProperties.JDBC_URL, props.getProperty("jdbc.url"));
		dbCfg.put(ConfigProperties.JDBC_USER, props.getProperty("jdbc.user"));
		dbCfg.put(ConfigProperties.JDBC_PASSWORD, props.getProperty("jdbc.password"));
		dbCfg.put(ConfigProperties.JDBC_GENERATION_STRATEGY, props.getProperty("jdbc.generation.strategy", "none"));
		dbCfg.put(ConfigProperties.JDBC_LOGGING, props.getProperty("jdbc.logging", "off"));
		
		Connection.setDBConfig(dbCfg);
		
	}
	
}