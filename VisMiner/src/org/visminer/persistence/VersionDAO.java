package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.Committer;
import org.visminer.model.Repository;
import org.visminer.model.Version;

public class VersionDAO{

	private Connection connection = Connection.getInstance();
	
	public Version save(Version version){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		version = em.merge(version);
		em.getTransaction().commit();
		em.close();
		return version;
		
	}
	
	public List<Version> getByRepository(Repository repository){
		
		EntityManager em = connection.getEntityManager();
		TypedQuery<Version> query = em.createQuery("select v from Version v where v.repository.idrepository=:arg1", Version.class);
		query.setParameter("arg1", repository.getIdGit());

		try{
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
		
	}
	
}
