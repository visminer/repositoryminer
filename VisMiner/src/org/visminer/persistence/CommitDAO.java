package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.Commit;
import org.visminer.model.Committer;

public class CommitDAO{

	private Connection connection = Connection.getInstance();
	
	public Commit save(Commit commit){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		commit = em.merge(commit);
		em.getTransaction().commit();
		em.close();
		return commit;
		
	}

	public List<Commit> getByCommitter(Committer committer){
		
		EntityManager em = connection.getEntityManager();
		TypedQuery<Commit> query = em.createQuery("select c from Commit c where c.committer = :arg1", Commit.class);
		query.setParameter("arg1", committer);
		try{
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
	}	
	
}