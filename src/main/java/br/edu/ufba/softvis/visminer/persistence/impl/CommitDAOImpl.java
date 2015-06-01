package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.CommitDB;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitDAO;

public class CommitDAOImpl extends DAOImpl<CommitDB, Integer> implements CommitDAO{

	@Override
	public Collection<CommitDB> findByNames(List<String> names) {
		
		if(names == null)
			return null;
		
		EntityManager em = getEntityManager();
		TypedQuery<CommitDB> query = em.createQuery("select new org.visminer.model.database.Commit(c.id)"
				+ " from Commit c where c.code in :names", CommitDB.class);
		query.setParameter("names", names);
		
		List<CommitDB> result = query.getResultList();
		return result;
		
	}

}