package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.Issue;
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
		
		TypedQuery<Milestone> query = em.createQuery("SELECT m FROM Milestone m WHERE m.number = :arg1 and" +
				" m.repository = :arg2", Milestone.class);
		
		query.setParameter("arg1", number);
		query.setParameter("arg2", repository);
		
		try{
			return query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}

	public List<Milestone> getAll(Repository repository){
	
		EntityManager em = connection.getEntityManager();
		
		TypedQuery<Milestone> query = em.createQuery("SELECT i FROM Milestone i WHERE i.repository = :arg1", Milestone.class);
		
		query.setParameter("arg1", repository);
		
		try{
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
	}
	
}
