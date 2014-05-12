package org.visminer.persistence;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.Repository;

/**
 * <p>
 * The operation class for the repository database table.
 * </p>
 * @author Felipe
 * @version 1.0
 */
public class RepositoryDAO{
	
	private Connection connection = Connection.getInstance();
	
	/**
	 * <p>
	 * saves the repository in database if id is set otherwise updates the repository in database
	 * </p>
	 * @param repository
	 * @return repository saved
	 */
	public Repository save(Repository repository){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		repository = em.merge(repository);
		em.getTransaction().commit();
		em.close();
		return repository;
		
	}
	
	/**
	 * 
	 * @param localPath : local repository path
	 * @return repository by local path
	 */
	public Repository getByPath(String localPath){
		
		EntityManager em = connection.getEntityManager();
		TypedQuery<Repository> query = em.createQuery("select r from Repository r where r.path = :arg1", Repository.class);
		query.setParameter("arg1", localPath);
		
		try{
			return query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
	
	/**
	 * 
	 * @param idGit : <repository owner>/<repository name>  
	 * @return repository by idGit
	 */
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
