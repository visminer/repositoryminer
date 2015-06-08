package br.edu.ufba.softvis.visminer.config;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.persistence.config.PersistenceUnitProperties;

public class DBConfig {

	public static final String JDBC_DRIVER = PersistenceUnitProperties.JDBC_DRIVER;
	public static final String JDBC_URL = PersistenceUnitProperties.JDBC_URL;
	public static final String JDBC_USER = PersistenceUnitProperties.JDBC_USER;
	public static final String JDBC_PASSWORD = PersistenceUnitProperties.JDBC_PASSWORD;
	public static final String DDL_GENERATION = PersistenceUnitProperties.DDL_GENERATION;
	public static final String LOGGING_LEVEL = PersistenceUnitProperties.LOGGING_LEVEL;

	private String driver;
	private String url;
	private String user;
	private String pass;
	private String generation;
	private String logging;

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

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
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
		map.put(JDBC_PASSWORD, this.pass);
		map.put(DDL_GENERATION, this.generation);
		map.put(LOGGING_LEVEL, this.logging);

		return map;
		
	}

}
