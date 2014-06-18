package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;


import org.visminer.model.Milestone;
import org.visminer.model.Repository;

public class MilestoneDAO{

	private Connection connection = Connection.getInstance();
	
	public Milestone save(Milestone milestone){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		milestone = em.merge(milestone);
		em.getTransaction().commit();
		em.close();
		return milestone;
		
	}
	
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
	
	public int deleteOne(int number, Repository repository){
		
		int qtdeDeleted = 0;
		IssueDAO id = new IssueDAO();
		Milestone m = getOne(number, repository);
		id.setNullMilestone(m);
				
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
