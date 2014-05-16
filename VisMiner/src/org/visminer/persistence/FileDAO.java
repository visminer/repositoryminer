package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.Commit;
import org.visminer.model.Committer;
import org.visminer.model.File;

public class FileDAO{

	private Connection connection = Connection.getInstance();
	
	public File save(File file){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		file = em.merge(file);
		em.getTransaction().commit();
		em.close();
		return file;
		
	}
	
	public List<File> getByCommit(Commit commit){
		
		EntityManager em = connection.getEntityManager();
		TypedQuery<File> query = em.createQuery("select f from File f where f.commit.sha = :arg1", File.class);
		query.setParameter("arg1", commit.getSha());
		try{
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
	}	
	
}
