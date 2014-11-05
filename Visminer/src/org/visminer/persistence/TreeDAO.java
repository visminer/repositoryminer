package org.visminer.persistence;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.database.Tree;

public class TreeDAO {

	public void saveAll(List<Tree> trees){
		
		EntityManager em = Connection.getEntityManager();
		em.getTransaction().begin();
		
		for(Tree tree : trees)
			em.merge(tree);
		
		em.getTransaction().commit();
		em.close();
		
	}
	
	public List<Tree> getByRepository(int repositoryId){
		
		EntityManager em = Connection.getEntityManager();
		TypedQuery<Tree> query = em.createQuery("select t from Tree t where t.repository.id = :arg0", Tree.class);
		query.setParameter("arg0", repositoryId);
		
		try{
			List<Tree> result = query.getResultList();
			em.close();
			return result;
		}catch(NoResultException e){
			return null;
		}
		
	}
	
}
