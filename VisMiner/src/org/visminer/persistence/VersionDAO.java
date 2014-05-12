package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.Repository;
import org.visminer.model.Version;

/**
 * <p>
 * The operation class for the version database table.
 * </p>
 * @author Felipe
 * @version 1.0
 */
public class VersionDAO{

	private Connection connection = Connection.getInstance();
	
	/**
	 * <p>
	 * saves the version in database if id is set otherwise updates the version in database
	 * </p>
	 * @param version
	 * @return version saved
	 */
	public Version save(Version version){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		version = em.merge(version);
		em.getTransaction().commit();
		em.close();
		return version;
		
	}
	
	/**
	 * 
	 * @param repository
	 * @return versions by repository
	 */
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
