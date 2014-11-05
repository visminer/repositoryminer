package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.database.File;

public class FileDAO{

	public File getBySha(String sha){
		
		EntityManager em = Connection.getEntityManager();
		TypedQuery<File> query = em.createQuery("select f from File f where f.sha = :arg0", File.class);
		query.setParameter("arg0", sha);
		
		try{
			File result = query.getSingleResult();
			em.close();
			return result;
		}catch(NoResultException e){
			return null;
		}
		
	}
	
	public File save(File file){
		
		EntityManager em = Connection.getEntityManager();
		em.getTransaction().begin();
		File f = em.merge(file);
		em.getTransaction().commit();
		em.close();
		return f;
		
	}
	
	public List<File> getByCommit(int idCommit){
		
		EntityManager em = Connection.getEntityManager();
		TypedQuery<File> query = em.createQuery("select f from File f join f.fileXCommits fxc where fxc.id.commitId = :arg0", File.class);
		query.setParameter("arg0", idCommit);
		
		try{
			List<File> files = query.getResultList();
			em.close();
			return files;
		}catch(NoResultException e){
			return null;
		}
		
	}
	
}