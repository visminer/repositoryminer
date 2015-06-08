package br.edu.ufba.softvis.visminer.persistence.impl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.CommitterDB;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitterDAO;

public class CommitterDAOImpl extends DAOImpl<CommitterDB, Integer> implements CommitterDAO {

	@Override
	public CommitterDB findByEmail(String email) {
		
		EntityManager em = getEntityManager();
		TypedQuery<CommitterDB> query = em.createQuery("select c from CommitterDB c where c.email = :email", CommitterDB.class);
		query.setParameter("email", email);
		
		try{
			return query.getSingleResult();
		}catch(Exception e){
			return null;
		}
		
	}

}
