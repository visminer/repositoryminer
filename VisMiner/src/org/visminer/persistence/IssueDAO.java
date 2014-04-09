package org.visminer.persistence;

import javax.persistence.EntityManager;

import org.visminer.model.Issue;

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
	
}
