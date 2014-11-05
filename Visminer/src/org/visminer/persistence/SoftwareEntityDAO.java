package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.database.SoftwareEntity;

public class SoftwareEntityDAO {

	public SoftwareEntity save(SoftwareEntity softwareEntty){
		
		EntityManager em = Connection.getEntityManager();
		em.getTransaction().begin();
		SoftwareEntity resp = em.merge(softwareEntty);
		em.getTransaction().commit();
		em.close();
		return resp;
		
	}
	
	public List<SoftwareEntity> getByCommitAndFile(int commitId, int fileId){
		
		EntityManager em = Connection.getEntityManager();
		TypedQuery<SoftwareEntity> query = em.createQuery("select s from SoftwareEntity s where s.fileXCommit.commit.id = :arg0 and s.fileXCommit.file.id = :arg1", SoftwareEntity.class);
		query.setParameter("arg0", commitId);
		query.setParameter("arg1", fileId);
		
		try{
			List<SoftwareEntity> resp = query.getResultList();
			em.close();
			return resp;
		}catch(NoResultException e){
			em.close();
			return null;
		}
		
	}
	
}
