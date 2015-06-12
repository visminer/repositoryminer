package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.persistence.dao.FileDAO;

public class FileDAOImpl extends DAOImpl<FileDB, Integer> implements FileDAO {

	@Override
	public Integer findIdByCode(String uid) {
		
		EntityManager em = getEntityManager();
		Query query = em.createNamedQuery("FileDB.findByCode", FileDB.class);
		query.setParameter("uid", uid);
		
		try{
			return (Integer) query.getSingleResult();
		}catch(Exception e){
			return 0;
		}
		
	}

	@Override
	public List<FileDB> findCommitedFiles(int commitId) {
		
		EntityManager em = getEntityManager();
		TypedQuery<FileDB> query = em.createNamedQuery("FileDB.findCommitedFiles", FileDB.class);
		query.setHint("javax.persistence.cache.storeMode", "BYPASS");
		query.setHint("javax.persistence.cache.retrieveMode", "BYPASS");
		query.setParameter("id", commitId);
		return query.getResultList();
		
	}

}