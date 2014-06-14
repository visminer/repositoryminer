package org.visminer.persistence;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.persistence.config.PersistenceUnitProperties;

public class ConfigConnectionSqlserver implements ConfigConnection{
	
	 public HashMap<String, String> sgbdConnection(){
		 
		 Map<String, String> props = new HashMap<String, String>();
			props.put(PersistenceUnitProperties.JDBC_DRIVER, "com.microsoft.jdbc.sqlserver.SQLServerDriver");
			props.put(PersistenceUnitProperties.JDBC_URL, "jdbc:microsoft:sqlserver://localhost:1433/visminer");
			props.put(PersistenceUnitProperties.JDBC_USER, "SqlServer");
			props.put(PersistenceUnitProperties.JDBC_PASSWORD, "root"); 
			props.put(PersistenceUnitProperties.DDL_GENERATION, "create-tables");
			Connection.setDataBaseInfo(props);
			
			return (HashMap<String, String>) props;
		 
	 }
}
