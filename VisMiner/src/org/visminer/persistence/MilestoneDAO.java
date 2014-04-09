package org.visminer.persistence;

import javax.persistence.EntityManager;

import org.visminer.model.Milestone;

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
	
}
