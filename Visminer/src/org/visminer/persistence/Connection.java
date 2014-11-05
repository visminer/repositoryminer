package org.visminer.persistence;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Connection {

	private static EntityManagerFactory managerFactory = null;	
	private final static String PERSISTENCE_UNIT = "VisMiner";
	
	public static void setDBConfig(Map<String, String> cfg){
		managerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, cfg);
	}
	
	public static void close(){
		managerFactory.close();
	}
	
	public static boolean isOpen(){
		return managerFactory.isOpen();
	}

	public static EntityManager getEntityManager(){
		return managerFactory.createEntityManager();
	}	
	
}