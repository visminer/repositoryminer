package org.visminer.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.kohsuke.github.GHIssueState;
import org.visminer.model.Issue;
import org.visminer.model.Milestone;
import org.visminer.model.Repository;

/**
 * <p>
 * The operation class for the milestone database table.
 * </p>
 * @author Heron
 * @version 1.0
 */
public class MilestoneDAO{

	private Connection connection = Connection.getInstance();
	
	/**
	 * <p>
	 * saves the milestone in database if id is set otherwise updates the milestone in database
	 * </p>
	 * @param milestone
	 * @return milestone saved
	 */
	public Milestone save(Milestone milestone){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		milestone = em.merge(milestone);
		em.getTransaction().commit();
		em.close();
		return milestone;
		
	}
	
	/**
	 * 
	 * @param number
	 * @param repository
	 * @return milestone by number and repository
	 */
	public Milestone getOne(int number, Repository repository){
		
		EntityManager em = connection.getEntityManager();
		
		TypedQuery<Milestone> query = em.createQuery("SELECT m FROM Milestone m WHERE m.number = :arg1 AND" +
				" m.repository = :arg2", Milestone.class);
		
		query.setParameter("arg1", number);
		query.setParameter("arg2", repository);
		
		try{
			return query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}
	
	/**
	 * 
	 * @param repository
	 * @return number of milestones deleted
	 */
	public int deleteAll(Repository repository){
		
		int qtdeDeletions = 0;
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		
		TypedQuery<Milestone> query = em.createQuery("DELETE FROM Milestone m WHERE m.repository = :arg1",
				Milestone.class);
		
		query.setParameter("arg1", repository);
		
		qtdeDeletions =  query.executeUpdate();
		em.getTransaction().commit();
		em.close();
			
		return qtdeDeletions;
	
		
	}
	
	/**
	 * 
	 * @param number
	 * @param repository
	 * @return 1 if milestone was deleted otherwise 0
	 */
	public int deleteOne(int number, Repository repository){
		
		int qtdeDeleted = 0;
				
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		
		TypedQuery<Milestone> query = em.createQuery("DELETE FROM Milestone m WHERE m.repository = :arg1"+
				" AND m.number = :arg2", Milestone.class);
		
		query.setParameter("arg1", repository);
		query.setParameter("arg2", number);
		
		qtdeDeleted = query.executeUpdate();
		em.getTransaction().commit();
		em.close();
		return qtdeDeleted;
		
	}

	/**
	 * 
	 * @param repository
	 * @return milestones by repository
	 */
	public List<Milestone> getAll(Repository repository){
	
		EntityManager em = connection.getEntityManager();
		
		TypedQuery<Milestone> query = em.createQuery("SELECT m FROM Milestone m WHERE m.repository = :arg1", Milestone.class);
		
		query.setParameter("arg1", repository);
		
		try{
			
			return query.getResultList();

		}catch(NoResultException e){
		
			return null;
		
		}
	}
	
	/**
	 * 
	 * @param repository
	 * @param state : milestone state can be "CLOSED" or "OPEN"
	 * @return milestones by repository and state
	 */
	public List<Milestone> getByStatus(Repository repository, String state){
		
		EntityManager em = connection.getEntityManager();
		
		TypedQuery<Milestone> query = em.createQuery("SELECT m FROM Milestone m WHERE m.repository = :arg1" +
				" AND m.state = :arg2", Milestone.class);
		
		query.setParameter("arg1", repository);
		query.setParameter("arg2", state);
		
		try{
			
			return query.getResultList();

		}catch(NoResultException e){
		
			return null;
		
		}
	}
	
}
