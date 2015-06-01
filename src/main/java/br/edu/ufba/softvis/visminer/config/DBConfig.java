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

	private String driver = null;
	private String url = null;
	private String user = null;
	private String pass = null;
	private String generation = null;
	private String name = null;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> toMap() {
		
		Map<String, String> map = new HashMap<String, String>(5);
		
		map.put(JDBC_DRIVER, getDriver());
		map.put(JDBC_URL, getUrl());
		map.put(JDBC_USER, getUser());
		map.put(JDBC_PASSWORD, getPass());
		map.put(DDL_GENERATION, getGeneration());

		return map;
		
	}

}
