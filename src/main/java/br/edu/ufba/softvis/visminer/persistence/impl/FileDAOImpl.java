package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.persistence.dao.FileDAO;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * 
 * Implementation of interface {@link FileDAO}
 */

public class FileDAOImpl extends DAOImpl<FileDB, Integer> implements FileDAO {

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