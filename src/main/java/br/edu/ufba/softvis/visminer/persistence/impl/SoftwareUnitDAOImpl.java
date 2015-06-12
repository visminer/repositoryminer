package br.edu.ufba.softvis.visminer.persistence.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitDB;
import br.edu.ufba.softvis.visminer.persistence.dao.SoftwareUnitDAO;

public class SoftwareUnitDAOImpl extends DAOImpl<SoftwareUnitDB, Integer> implements SoftwareUnitDAO{

	public SoftwareUnitDB findByUid(String uid) {
		
		EntityManager em = getEntityManager();
		TypedQuery<SoftwareUnitDB> query = em.createNamedQuery("SoftwareUnitDB.findByUid", SoftwareUnitDB.class);
		query.setParameter("uid", uid);
		
		try{
			return query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}

}
