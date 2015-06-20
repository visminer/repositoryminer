package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.CommitterDB;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitterDAO;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * 
 * Implementation of interface {@link CommitterDAO}
 */

public class CommitterDAOImpl extends DAOImpl<CommitterDB, Integer> implements CommitterDAO {

	@Override
	public CommitterDB findByEmail(String email) {
		
		EntityManager em = getEntityManager();
		TypedQuery<CommitterDB> query = em.createNamedQuery("CommitterDB.findByEmail", CommitterDB.class);
		query.setParameter("email", email);
		
		try{
			return query.getSingleResult();
		}catch(Exception e){
			return null;
		}
		
	}

	@Override
	public List<CommitterDB> findByRepository(int repositoryId) {
	
		EntityManager em = getEntityManager();
		TypedQuery<CommitterDB> query = em.createNamedQuery("CommitterDB.findByRepository", CommitterDB.class);
		query.setParameter("id", repositoryId);
		return query.getResultList();
		
	}

}
