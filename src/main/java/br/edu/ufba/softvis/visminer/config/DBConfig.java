package br.edu.ufba.softvis.visminer.config;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import br.edu.ufba.softvis.visminer.utility.PropertyReader;

/**
 * @version 0.9
 * Database configuration bean.
 */
public class DBConfig {

	public static final String JDBC_DRIVER = PersistenceUnitProperties.JDBC_DRIVER;
	public static final String JDBC_URL = PersistenceUnitProperties.JDBC_URL;
	public static final String JDBC_USER = PersistenceUnitProperties.JDBC_USER;
	public static final String JDBC_PASSWORD = PersistenceUnitProperties.JDBC_PASSWORD;
	public static final String DDL_GENERATION = PersistenceUnitProperties.DDL_GENERATION;
	public static final String LOGGING_LEVEL = PersistenceUnitProperties.LOGGING_LEVEL;
	
	public static final String PROP_JDBC_DRIVER = "jdbc.driver";
	public static final String PROP_JDBC_URL = "jdbc.url";
	public static final String PROP_JDBC_USER = "jdbc.user";
	public static final String PROP_JDBC_PASSWORD = "jdbc.password";
	public static final String PROP_JDBC_GENERATION = "jdbc.tables.generation";
	public static final String PROP_JDBC_LOGGING = "jdbc.logging";

	private String driver;
	private String url;
	private String user;
	private String password;
	private String generation;
	private String logging;

	public DBConfig(){}
	
	public DBConfig(PropertyReader properties){
		
		this.driver = properties.getProperty(PROP_JDBC_DRIVER);
		this.url = properties.getProperty(PROP_JDBC_URL);
		this.user = properties.getProperty(PROP_JDBC_USER);
		this.password = properties.getProperty(PROP_JDBC_PASSWORD);
		this.generation = properties.getProperty(PROP_JDBC_GENERATION);
		this.logging = properties.getProperty(PROP_JDBC_LOGGING);
		
	}	
	
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGeneration() {
		return generation;
	}

	public void setGeneration(String generation) {
		this.generation = generation;
	}

	public String getLogging() {
		return logging;
	}

	public void setLogging(String logging) {
		this.logging = logging;
	}

	public Map<String, String> toMap() {
		
		Map<String, String> map = new HashMap<String, String>(6);
		
		map.put(JDBC_DRIVER, this.driver);
		map.put(JDBC_URL, this.url);
		map.put(JDBC_USER, this.user);
		map.put(JDBC_PASSWORD, this.password);
		map.put(DDL_GENERATION, this.generation);
		map.put(LOGGING_LEVEL, this.logging);

		return map;
		
	}
}
