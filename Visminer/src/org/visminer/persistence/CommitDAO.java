package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.database.Commit;


public class CommitDAO{

	public List<Commit> getByNames(List<String> names){
		
		EntityManager em = Connection.getEntityManager();
		TypedQuery<Commit> query = em.createQuery("select c from Commit c where c.name in :arg0", Commit.class);
		query.setParameter("arg0", names);
		
		try{
			List<Commit> result = query.getResultList();
			em.close();
			return result;
		}catch(NoResultException e){
			return null;
		}
		
	}
	
	
	public List<Commit> getByTree(int treeId){
		
		EntityManager em = Connection.getEntityManager();
		TypedQuery<Commit> query = em.createQuery("select c from Commit c join c.trees t where t.id = :arg0 order by c.date asc", Commit.class);
		query.setParameter("arg0", treeId);
		
		try{
			List<Commit> result = query.getResultList();
			em.close();
			return result;
		}catch(NoResultException e){
			return null;
		}
		
	}
	
	public List<Commit> getByRepository(int repositoryId){
		
		EntityManager em = Connection.getEntityManager();
		TypedQuery<Commit> query = em.createQuery("select c from Commit c join c.trees t join t.repository r where r.id = :arg0", Commit.class);
		query.setParameter("arg0", repositoryId);
		
		try{
			List<Commit> resp = query.getResultList();
			em.close();
			return resp;
		}catch(NoResultException e){
			return null;
		}
		
	}
	
}