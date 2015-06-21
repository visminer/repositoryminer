package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitDAO;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * 
 * Implementation of interface {@link CommitDAO}
 */

public class CommitDAOImpl extends DAOImpl<CommitDB, Integer> implements CommitDAO{

	@Override
	public List<CommitDB> findByTree(int treeId) {
		
		EntityManager em = getEntityManager();
		TypedQuery<CommitDB> query = em.createNamedQuery("CommitDB.findByTree", CommitDB.class);
		query.setParameter("id", treeId);
		return query.getResultList();
		
	}


	
}