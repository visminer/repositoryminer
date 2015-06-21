package br.edu.ufba.softvis.visminer.persistence;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * 
 * Management of database connection.
 */

public class Database {

	private final static String PERSISTENCE_UNIT = "VisminerDatabase";
	private static EntityManagerFactory managerFactory = null;
	private static Database instance = null;
	
	private Database(){}
	
	/**
	 * 
	 * @return Database manager instance
	 */
	public static Database getInstance(){
		if(instance == null){
			instance = new Database();
		}
		return instance;
	}
	
	/**
	 * @param cfg
	 * Opens database connection.
	 */
	public void open(Map<String, String> cfg){
		managerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, cfg);
	}
	
	/**
	 * Closes database connection.
	 */
	public void close(){
		managerFactory.close();
	}
	
	/**
	 * 
	 * @return Used properties to configure database connection.
	 */
	public Map<String, Object> getProperties(){
		return managerFactory.getProperties();
	}
	
	/**
	 * 
	 * @return True if database connection was correctly configured or false otherwise.
	 */
	public boolean isOpen(){
		return managerFactory.isOpen();
	}

	/**
	 * 
	 * @return Database connection instance.
	 */
	public EntityManager getEntityManager(){
		return managerFactory.createEntityManager();
	}	
	
}