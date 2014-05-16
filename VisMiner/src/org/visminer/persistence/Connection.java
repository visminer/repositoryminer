package org.visminer.persistence;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class Connection {

	private static Connection instance = null;
	private EntityManagerFactory managerFactory;
	private static Map<String, String> properties;
	
	private Connection(){
		
		managerFactory = Persistence.createEntityManagerFactory("VisMiner", properties);
		
	}

	public static void setDataBaseInfo(Map<String, String> myProperties){
		
		properties = myProperties;
		
	}

	public static Connection getInstance(){
		
		if(instance == null)
			instance = new Connection();
		
		return instance;
		
	}

	public void close(){
		
		managerFactory.close();
		
	}
	
	public EntityManager getEntityManager(){
		
		return managerFactory.createEntityManager();
		
	}	
	
}
