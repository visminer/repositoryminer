package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.visminer.model.Issue;
import org.visminer.model.Label;

public class LabelDAO {

	private Connection connection = Connection.getInstance();
	
	public Label save(Label label){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		label = em.merge(label);
		em.getTransaction().commit();
		em.close();
		return label;
		
	}
	
	public List<Label> getByIssue(Issue issue){
		
		EntityManager em = connection.getEntityManager();
		TypedQuery<Label> query = em.createQuery("select l from Label l where l.issue.number = :arg0 and l.issue.repository.idGit = :arg1", Label.class);
		query.setParameter("arg0", issue.getId().getNumber());
		query.setParameter("arg1", issue.getId().getRepositoryIdGit());
		try{
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
		
	}
	
}