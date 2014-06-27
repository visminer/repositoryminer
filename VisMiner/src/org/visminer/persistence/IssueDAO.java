package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.Issue;
import org.visminer.model.Milestone;
import org.visminer.model.Repository;

public class IssueDAO{

	private Connection connection = Connection.getInstance();
	
	public Issue save(Issue issue){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		issue = em.merge(issue);
		em.getTransaction().commit();
		em.close();
		return issue;
		
	}
	
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
	
	/**
	 * update to null all Milestone (a specified) referencing in the Issues
	 * @param milestone
	 */
	public void setNullMilestone(Milestone milestone){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		
		for(Issue i : getAllbyMilestone(milestone)){
			i.setMilestone(null);
			em.merge(i);
		}
		
		em.getTransaction().commit();
		em.close();
		
	}
	
	public List<Issue> getIssuesReferencedInCommitsByRepository(Repository repository){
		
		EntityManager em = connection.getEntityManager();
		
		TypedQuery<Issue> query = em.createQuery("Select i from Issue i join i.commits c"
				+" where i.repository =:arg1", Issue.class);
		
		query.setParameter("arg1", repository);
		
		try{
			
			return query.getResultList();
			
		}catch(NoResultException e){
			
			return null;
		}
		
	}
	
}
