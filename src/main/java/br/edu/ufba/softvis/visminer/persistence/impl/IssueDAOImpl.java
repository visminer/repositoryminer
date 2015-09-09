package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.edu.ufba.softvis.visminer.model.database.IssueDB;
import br.edu.ufba.softvis.visminer.persistence.dao.IssueDAO;

/**
 * Implementation of interface {@link IssueDAO}
 */

public class IssueDAOImpl extends DAOImpl<IssueDB, Integer> implements IssueDAO {

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, Integer> minimalFindByRepository(int repositoryId) {
		
		EntityManager em = getEntityManager();
		Query query = em.createNamedQuery("IssueDB.minFindByRepository");
		query.setParameter("id", repositoryId);
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		List<Object[]> objs = query.getResultList();
		
		if(objs.size() == 0) return null;
		
		for(Object[] obj : objs){
			map.put( (Integer)obj[0], (Integer)obj[1]);
		}
		return map;
		
	}


}
