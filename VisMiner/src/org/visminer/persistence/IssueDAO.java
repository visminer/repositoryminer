package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.Issue;
import org.visminer.model.Metric;
import org.visminer.model.Milestone;
import org.visminer.model.Repository;

/**
 * <p>
 * The operation class for the issue database table.
 * </p>
 * @author Heron
 * @version 1.0
 */
public class IssueDAO{

	private Connection connection = Connection.getInstance();
	
	/**
	 * <p>
	 * saves the issue in database if id is set otherwise updates the issue in database
	 * </p>
	 * @param issue
	 * @return issue saved
	 */
	public Issue save(Issue issue){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		issue = em.merge(issue);
		em.getTransaction().commit();
		em.close();
		return issue;
		
	}
	
	/**
	 * 
	 * @param number
	 * @param repository
	 * @return issue by number and repository
	 */
	public Issue getOne(int number, Repository repository){
			
			EntityManager em = connection.getEntityManager();
			
			TypedQuery<Issue> query = em.createQuery("SELECT i FROM Issue i WHERE i.number = :arg1 and" +
					" i.repository = :arg2", Issue.class);
			
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
	 * @return issues by repository
	 */
	public List<Issue> getAll(Repository repository){
		
		EntityManager em = connection.getEntityManager();
		
		TypedQuery<Issue> query = em.createQuery("SELECT i FROM Issue i WHERE i.repository = :arg1", Issue.class);
		
		query.setParameter("arg1", repository);
		
		try{
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
	}
	
	/**
	 * 
	 * @param milestone
	 * @return issues by milestone
	 */
	public List<Issue> getAllbyMilestone(Milestone milestone){
		
		EntityManager em = connection.getEntityManager();
		
		TypedQuery<Issue> query = em.createQuery("SELECT i FROM Issue i WHERE i.milestone = :arg1", Issue.class);
		
		query.setParameter("arg1", milestone);
		
		try{
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
	}
	
}
