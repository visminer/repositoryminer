package org.visminer.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.database.Committer;

public class CommitterDAO{
	
	public Committer getByNameAndEmail(String name, String email){
		
		EntityManager em = Connection.getEntityManager();
		TypedQuery<Committer> query = em.createQuery("select c from Committer c where c.email = :arg0 and c.name = :arg1", Committer.class);
		query.setParameter("arg0", email);
		query.setParameter("arg1", name);
		
		try{
			Committer result = query.getSingleResult();
			em.close();
			return result;
		}catch(NoResultException e){
			return null;
		}
		
	}
	
	public List<Committer> saveAll(List<Committer> committers){
		
		EntityManager em = Connection.getEntityManager();
		em.getTransaction().begin();
		List<Committer> committers2 = new ArrayList<Committer>();
		
		for(Committer c : committers){
			committers2.add(em.merge(c));
		}
		
		em.getTransaction().commit();
		em.close();
		
		return committers2;
		
	}
	
	public List<Committer> getAll(){
		
		EntityManager em = Connection.getEntityManager();
		TypedQuery<Committer> query = em.createQuery("select c from Committer c", Committer.class);
		
		try{
			List<Committer> result = query.getResultList();
			em.close();
			return result;
		}catch(NoResultException e){
			return null;
		}
		
	}
	
	public List<Committer> getByRepository(int repositoryId){
		
		EntityManager em = Connection.getEntityManager();
		TypedQuery<Committer> query = em.createQuery("select c from Committer c join c.repositories r where r.id = :arg0", Committer.class);
		query.setParameter("arg0", repositoryId);
		
		try{
			List<Committer> result = query.getResultList();
			em.close();
			return result;
		}catch(NoResultException e){
			return null;
		}
		
	}
	
}