package br.edu.ufba.softvis.visminer.config;

import java.util.HashMap;
import java.util.Map;

import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_DRIVER;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_URL;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_USER;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_PASSWORD;
import static org.eclipse.persistence.config.PersistenceUnitProperties.DDL_GENERATION;
import static org.eclipse.persistence.config.PersistenceUnitProperties.LOGGING_LEVEL;

import br.edu.ufba.softvis.visminer.utility.PropertyReader;

/**
 * Database configuration bean.
 */
public class DBConfig {

  // properties file constants
  private static final String PROPS_NAMESPACE = "visminer.";
  private static final String PROPS_JDBC_DRIVER = PROPS_NAMESPACE + "jdbc.driver";
  private static final String PROPS_JDBC_URL = PROPS_NAMESPACE + "jdbc.url";
  private static final String PROPS_JDBC_USER = PROPS_NAMESPACE + "jdbc.user";
  private static final String PROPS_JDBC_PASSWORD = PROPS_NAMESPACE + "jdbc.password";
  private static final String PROPS_JDBC_DDL_GENERATION = PROPS_NAMESPACE + "jdbc.ddl-generation";
  private static final String PROPS_JDBC_LOGGING_LEVEL = PROPS_NAMESPACE + "jdbc.logging-level";
  
  // Default configuration values
  private static final String DEFAULT_LOGGING_LEVEL = "off";
  private static final String DEFAULT_DDL_GENERATION = "create-tables";
  
  private String driver;
  private String url;
  private String user;
  private String password;
  private String ddlGeneration = DEFAULT_DDL_GENERATION;
  private String loggingLevel = DEFAULT_LOGGING_LEVEL;

  public DBConfig(){}

  public DBConfig(PropertyReader properties){
    
    this.driver = properties.getProperty(PROPS_JDBC_DRIVER);
    this.url = properties.getProperty(PROPS_JDBC_URL);
    this.user = properties.getProperty(PROPS_JDBC_USER);
    this.password = properties.getProperty(PROPS_JDBC_PASSWORD);
    this.ddlGeneration = properties.getProperty(PROPS_JDBC_DDL_GENERATION, DEFAULT_DDL_GENERATION);
    this.loggingLevel = properties.getProperty(PROPS_JDBC_LOGGING_LEVEL, DEFAULT_LOGGING_LEVEL);
    
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

  public String getDdlGeneration() {
    return ddlGeneration;
  }

  public void setDdlGeneration(String ddlGeneration) {
    this.ddlGeneration = ddlGeneration;
  }

  public String getLoggingLevel() {
    return loggingLevel;
  }

  public void setLoggingLevel(String loggingLevel) {
    this.loggingLevel = loggingLevel;
  }

  public Map<String, String> toMap() {

    Map<String, String> map = new HashMap<String, String>(6);

    map.put(JDBC_DRIVER, this.driver);
    map.put(JDBC_URL, this.url);
    map.put(JDBC_USER, this.user);
    map.put(JDBC_PASSWORD, this.password);
    map.put(DDL_GENERATION, this.ddlGeneration);
    map.put(LOGGING_LEVEL, this.loggingLevel);

    return map;

  }
}
