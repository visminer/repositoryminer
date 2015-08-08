package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.persistence.dao.RepositoryDAO;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * 
 * Implementation of interface {@link RepositoryDAO}
 */

public class RepositoryDAOImpl extends DAOImpl<RepositoryDB, Integer> implements RepositoryDAO{

	@Override
	public List<RepositoryDB> findAll() {
		
		EntityManager em = getEntityManager();
		TypedQuery<RepositoryDB> query = em.createNamedQuery("RepositoryDB.findAll", RepositoryDB.class);
		try{
			return query.getResultList();
		}catch(Exception e){
			return null;
		}
	}
	
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
