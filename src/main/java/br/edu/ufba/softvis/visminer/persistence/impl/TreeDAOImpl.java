package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.TreeDB;
import br.edu.ufba.softvis.visminer.persistence.dao.TreeDAO;

public class TreeDAOImpl extends DAOImpl<TreeDB, Integer> implements TreeDAO{

	@Override
	public List<TreeDB> findByRepository(int repositoryId) {
		
		EntityManager em = getEntityManager();
		TypedQuery<TreeDB> query = em.createNamedQuery("TreeDB.findByRepository", TreeDB.class);
		query.setParameter("repositoryId", repositoryId);
		return query.getResultList();
		
	}

}
