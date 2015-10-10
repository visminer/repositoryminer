package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.IssueDB;
import br.edu.ufba.softvis.visminer.persistence.dao.IssueDAO;

/**
 * Implementation of interface {@link IssueDAO}
 */
@SuppressWarnings("unchecked")
public class IssueDAOImpl extends DAOImpl<IssueDB, Integer> implements IssueDAO {

	@Override
	public Map<Integer, Integer> minimalFindByRepository(int repositoryId) {
		
		EntityManager em = getEntityManager();
		Query query = em.createNamedQuery("IssueDB.minFindByRepository");
		query.setParameter("repositoryId", repositoryId);
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		List<Object[]> objs = query.getResultList();
		
		if(objs == null) return map;
		
		for(Object[] obj : objs){
			map.put( (Integer)obj[0], (Integer)obj[1]);
		}
		return map;
		
	}

	
	@Override
	public List<IssueDB> findByRepository(int repositoryId) {
		
		EntityManager em = getEntityManager();
		TypedQuery<IssueDB> query = em.createNamedQuery("IssueDB.findByRepository", IssueDB.class);
		query.setParameter("repositoryId", repositoryId);
		return getResultList(query);
		
	}	

}
