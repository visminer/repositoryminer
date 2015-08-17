package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitDAO;
import br.edu.ufba.softvis.visminer.utility.StringUtils;

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

	@Override
	public List<CommitDB> findByRepository(String repositoryPath) {
		
		String uid = StringUtils.sha1(repositoryPath.replace("\\", "/"));
		
		EntityManager em = getEntityManager();
		TypedQuery<CommitDB> query = em.createNamedQuery("CommitDB.findByRepository", CommitDB.class);
		query.setParameter("uid", uid);
		return query.getResultList();
		
	}
	
	


	
}