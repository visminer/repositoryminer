package br.edu.ufba.softvis.visminer.retriever;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.persistence.Database;
import br.edu.ufba.softvis.visminer.persistence.dao.DAO;

public abstract class Retriever {

	private EntityManager entityManager;
	
	public <DAOType extends DAO<?, ?>> DAOType newDAO(Class<?> clazz) {
		try {
			@SuppressWarnings("unchecked")
			DAOType dao = (DAOType) clazz.newInstance();
			dao.setEntityManager(entityManager = Database.getInstance().getEntityManager());
			
			return dao;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void closeDAO(DAO<?, ?> dao) {
		dao.getEntityManager().close();
	}
	
	public void shareEntityManager(DAO<?, ?> dao) {
		dao.setEntityManager(entityManager);
	}

}
