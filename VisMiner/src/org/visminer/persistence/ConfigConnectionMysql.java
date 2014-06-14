package org.visminer.persistence;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.persistence.config.PersistenceUnitProperties;

public class ConfigConnectionMysql implements ConfigConnection{
	
	 public HashMap<String, String> sgbdConnection(){
		 
		 Map<String, String> props = new HashMap<String, String>();
			props.put(PersistenceUnitProperties.JDBC_DRIVER, "com.mysql.jdbc.Driver");
			props.put(PersistenceUnitProperties.JDBC_URL, "jdbc:mysql://localhost/visminer");
			props.put(PersistenceUnitProperties.JDBC_USER, "root");
			props.put(PersistenceUnitProperties.JDBC_PASSWORD, "root"); 
			props.put(PersistenceUnitProperties.DDL_GENERATION, "create-tables");
			Connection.setDataBaseInfo(props);
			
			return (HashMap<String, String>) props;
		 
	 }
}
