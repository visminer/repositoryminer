package org.visminer.persistence;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.persistence.config.PersistenceUnitProperties;

public class ConfigConnectionPostgre implements ConfigConnection{
	
	 public HashMap<String, String> sgbdConnection(){
		 
		 Map<String, String> props = new HashMap<String, String>();
			props.put(PersistenceUnitProperties.JDBC_DRIVER, "org.postgresql.Driver");
			props.put(PersistenceUnitProperties.JDBC_URL, "jdbc:postgresql://localhost/visminer");
			props.put(PersistenceUnitProperties.JDBC_USER, "postgres");
			props.put(PersistenceUnitProperties.JDBC_PASSWORD, "root"); 
			props.put(PersistenceUnitProperties.DDL_GENERATION, "create-tables");
			Connection.setDataBaseInfo(props);
			
			return (HashMap<String, String>) props;
		 
	 }
}
