package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.database.Repository;

public class RepositoryDAO{
	
	public Repository save(Repository repository){
		
		EntityManager em = Connection.getEntityManager();
		em.getTransaction().begin();
		repository = em.merge(repository);
		em.getTransaction().commit();
		em.close();
		return repository;
		
	}
	
	public Repository getByPath(String sha){
		
		EntityManager em = Connection.getEntityManager();
		TypedQuery<Repository> query = em.createQuery("select r from Repository r where r.sha = :arg0", Repository.class);
		query.setParameter("arg0", sha);
		try{
			Repository r = query.getSingleResult();
			em.close();
			return r;
		}catch(NoResultException e){
			em.close();
			return null;
		}
		
	}
	
	public List<Repository> getByCommitter(int committerId){
		
		EntityManager em = Connection.getEntityManager();
		TypedQuery<Repository> query = em.createQuery("select r from Repository r join r.committers c where c.id = :arg0", Repository.class);
		query.setParameter("arg0", committerId);
		
		try{
			List<Repository> r = query.getResultList();
			em.close();
			return r;
		}catch(NoResultException e){
			em.close();
			return null;
		}
		
	}

	public List<Repository> getAll(){
		
		EntityManager em = Connection.getEntityManager();
		TypedQuery<Repository> query = em.createQuery("select r from Repository r", Repository.class);
		
		try{
			List<Repository> r = query.getResultList();
			em.close();
			return r;
		}catch(NoResultException e){
			em.close();
			return null;
		}
		
	}
	
}
