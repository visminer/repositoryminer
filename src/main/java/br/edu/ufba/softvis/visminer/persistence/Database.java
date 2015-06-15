package br.edu.ufba.softvis.visminer.persistence;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Database {

	private final static String PERSISTENCE_UNIT = "VisminerDatabase";
	private static EntityManagerFactory managerFactory = null;
	private static Database instance = null;
	
	private Database(){}
	
	public static Database getInstance(){
		if(instance == null){
			instance = new Database();
		}
		return instance;
	}
	
	public void open(Map<String, String> cfg){
		managerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, cfg);
	}
	
	public void close(){
		managerFactory.close();
	}
	
	public Map<String, Object> getProperties(){
		return managerFactory.getProperties();
	}
	
	public boolean isOpen(){
		return managerFactory.isOpen();
	}

	public EntityManager getEntityManager(){
		return managerFactory.createEntityManager();
	}	
	
}