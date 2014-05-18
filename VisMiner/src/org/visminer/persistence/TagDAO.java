package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.Repository;
import org.visminer.model.Tag;

public class TagDAO {


	private Connection connection = Connection.getInstance();
	
	public Tag save(Tag tag){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		tag = em.merge(tag);
		em.getTransaction().commit();
		em.close();
		return tag;
		
	}
	
	public void saveMany(List<Tag> tags){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		
		for(Tag tag : tags){
			tag = em.merge(tag);
		}
		
		em.getTransaction().commit();
		em.close();		
		
	}
	
	public List<Tag> getByRepository(Repository repository){
		
		EntityManager em = connection.getEntityManager();
		TypedQuery<Tag> query = em.createQuery("select t from Tag t where t.repository.idGit = :arg0", Tag.class);
		query.setParameter("arg0", repository.getIdGit());
		try{
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
	}

	
}