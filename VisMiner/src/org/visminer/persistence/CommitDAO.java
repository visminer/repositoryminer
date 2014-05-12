package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.Commit;
import org.visminer.model.Committer;
import org.visminer.model.Version;

/**
 * <p>
 * The operation class for the commit database table.
 * </p>
 * @author Felipe
 * @version 1.0
 */
public class CommitDAO{

	private Connection connection = Connection.getInstance();
	
	/**
	 * <p>
	 * saves the commit in database if id is set otherwise updates the commit in database
	 * </p>
	 * @param commit
	 * @return commit saved
	 */
	public Commit save(Commit commit){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		commit = em.merge(commit);
		em.getTransaction().commit();
		em.close();
		return commit;
		
	}
	
	/**
	 * 
	 * @param version
	 * @param committer
	 * @return commits by verion and committer
	 */
	public List<Commit> getByVersionAndCommitter(Version version, Committer committer){
		
		EntityManager em = connection.getEntityManager();
		TypedQuery<Commit> query = em.createQuery("select c from Commit c where c.version.idversion = :arg1 and c.committer.idcommitter = :arg2", Commit.class);
		query.setParameter("arg1", version.getIdversion());
		query.setParameter("arg2", committer.getIdcommitter());
		try{
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
	}
	
	/**
	 * 
	 * @param version
	 * @return commits by version
	 */
	public List<Commit> getByVersion(Version version){
		
		EntityManager em = connection.getEntityManager();
		TypedQuery<Commit> query = em.createQuery("select c from Commit c where c.version.idversion = :arg1", Commit.class);
		query.setParameter("arg1", version.getIdversion());
		try{
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
	}	
	
	/**
	 * 
	 * @param committer
	 * @return commits by committer
	 */
	public List<Commit> getByCommitter(Committer committer){
		
		EntityManager em = connection.getEntityManager();
		TypedQuery<Commit> query = em.createQuery("select c from Commit c where c.committer.idcommitter = :arg1", Commit.class);
		query.setParameter("arg1", committer.getIdcommitter());
		try{
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
	}	
	
}
