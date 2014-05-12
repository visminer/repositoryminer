package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.Committer;
import org.visminer.model.Repository;

/**
 * <p>
 * The operation class for the committer database table.
 * </p>
 * @author Felipe
 * @version 1.0
 */
public class CommitterDAO{
	
	private Connection connection = Connection.getInstance();
	
	/**
	 * <p>
	 * saves the committer in database if id is set otherwise updates the committer in database
	 * </p>
	 * @param committer
	 * @return committer saved
	 */
	public Committer save(Committer committer){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		committer = em.merge(committer);
		em.getTransaction().commit();
		em.close();
		return committer;
		
	}
	
	/**
	 * 
	 * @param repository
	 * @return committers by repository
	 */
	public List<Committer> getByRepository(Repository repository){
		
		EntityManager em = connection.getEntityManager();
		TypedQuery<Committer> query = em.createQuery("select c from Committer c where c.repository.idGit=:arg1", Committer.class);
		query.setParameter("arg1", repository.getIdGit());
		
		try{
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
		
	}
	
}
