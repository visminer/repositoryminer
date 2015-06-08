package br.edu.ufba.softvis.visminer.persistence.impl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.persistence.dao.RepositoryDAO;

public class RepositoryDAOImpl extends DAOImpl<RepositoryDB, Integer> implements RepositoryDAO{

	@Override
	public RepositoryDB getByUid(String uid) {
		
		EntityManager em = getEntityManager();
		TypedQuery<RepositoryDB> query = em.createQuery("select r from RepositoryDB r where r.uid = :uid", RepositoryDB.class);
		query.setParameter("uid", uid);
		try{
			return query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

}
