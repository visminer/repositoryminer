package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitDB;
import br.edu.ufba.softvis.visminer.persistence.dao.SoftwareUnitDAO;

/**
 * Implementation of interface {@link SoftwareUnitDAO}
 */

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

	@Override
	public List<SoftwareUnitDB> findByFile(int fileId) {
		
		EntityManager em = getEntityManager();
		TypedQuery<SoftwareUnitDB> query = em.createNamedQuery("SoftwareUnitDB.findByFile", SoftwareUnitDB.class);
		query.setParameter("id", fileId);
		
		return query.getResultList();

	}

	@Override
	public List<SoftwareUnitDB> findByRepository(int repositoryId, int commitId) {

		EntityManager em = getEntityManager();
		TypedQuery<SoftwareUnitDB> query = em.createNamedQuery("SoftwareUnitDB.findByRepository", SoftwareUnitDB.class);
		query.setParameter("repositoryId", repositoryId);
		query.setParameter("commitId", commitId);
		return query.getResultList();
		
	}

}