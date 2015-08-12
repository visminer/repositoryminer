package br.edu.ufba.softvis.visminer.retriever;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.persistence.Database;
import br.edu.ufba.softvis.visminer.persistence.dao.DAO;

public class Retriever {

	private EntityManager entityManager;
	
	public void shareEntityManager(DAO<?, ?> dao){
		dao.setEntityManager(entityManager);
	}
	
	public void closeEntityManager(){
		this.entityManager.close();
	}
	
	public void createEntityManager(){
		this.entityManager = Database.getInstance().getEntityManager();
	}
	
}
