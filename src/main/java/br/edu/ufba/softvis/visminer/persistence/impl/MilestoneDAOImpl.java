package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.MilestoneDB;
import br.edu.ufba.softvis.visminer.persistence.dao.MilestoneDAO;

/**
 * Implementation of interface {@link MilestoneDAO}
 */

public class MilestoneDAOImpl extends DAOImpl<MilestoneDB, Integer> implements MilestoneDAO{

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, Integer> minimalFindByRepository(int repositoryId) {
		
		EntityManager em = getEntityManager();
		Query query = em.createNamedQuery("MilestoneDB.minFindByRepository");
		query.setParameter("repositoryId", repositoryId);
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		List<Object[]> objs = query.getResultList();
		
		if(objs.size() == 0) return null;
		
		for(Object[] obj : objs){
			map.put( (Integer)obj[0], (Integer)obj[1]);
		}
		
		return map;
		
	}
	
	@Override
	public List<MilestoneDB> findByRepository(int repositoryId) {
		
		EntityManager em = getEntityManager();
		TypedQuery<MilestoneDB> query = em.createNamedQuery("MilestoneDB.findByRepository", MilestoneDB.class);
		query.setParameter("repositoryId", repositoryId);
		return query.getResultList();
		
	}	

}