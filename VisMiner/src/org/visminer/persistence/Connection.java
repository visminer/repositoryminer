package org.visminer.persistence;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * <p>
 * Database connection management 
 * </p>
 * @author felipe
 * @version 1.0
 */
public class Connection {

	private static Connection instance = null;
	private EntityManagerFactory managerFactory;
	private static Map<String, String> properties;
	
	private Connection(){
		
		managerFactory = Persistence.createEntityManagerFactory("VisMiner", properties);
		
	}

	/**
	 * <p>
	 * Set database connection informations
	 * </p>
	 * @param myProperties
	 */
	public static void setDataBaseInfo(Map<String, String> myProperties){
		
		properties = myProperties;
		
	}

	/**
	 * 
	 * @return database connection
	 */
	public static Connection getInstance(){
		
		if(instance == null)
			instance = new Connection();
		
		return instance;
		
	}

	/**
	 * <p>
	 * close database connection
	 * </p>
	 */
	public void close(){
		
		managerFactory.close();
		
	}
	
	/**
	 * 
	 * @return entity manager
	 */
	public EntityManager getEntityManager(){
		
		return managerFactory.createEntityManager();
		
	}	
	
}
