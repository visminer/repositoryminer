package org.visminer.persistence;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.Repository;

public class RepositoryDAO{
	
	private Connection connection = Connection.getInstance();
	
	public Repository save(Repository repository){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		repository = em.merge(repository);
		em.getTransaction().commit();
		em.close();
		return repository;
		
	}
	
	public Repository getByPath(String path){
		
		EntityManager em = connection.getEntityManager();
		TypedQuery<Repository> query = em.createQuery("select r from Repository r where r.path = :arg1", Repository.class);
		query.setParameter("arg1", path);
		
		try{
			return query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
	
	public Repository getByIdGit(String idGit){
		
		EntityManager em = connection.getEntityManager();
		TypedQuery<Repository> query = em.createQuery("select r from Repository r where r.idGit = :arg1", Repository.class);
		query.setParameter("arg1", idGit);
		
		try{
			return query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

}
