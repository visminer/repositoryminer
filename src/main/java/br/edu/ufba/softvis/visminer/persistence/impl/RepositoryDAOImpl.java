package br.edu.ufba.softvis.visminer.persistence.impl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.persistence.dao.RepositoryDAO;

public class RepositoryDAOImpl extends DAOImpl<RepositoryDB, Integer> implements RepositoryDAO{

	@Override
	public RepositoryDB findByUid(String uid) {
		
		EntityManager em = getEntityManager();
		TypedQuery<RepositoryDB> query = em.createNamedQuery("RepositoryDB.findByUid", RepositoryDB.class);
		query.setParameter("uid", uid);
		try{
			return query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

}
