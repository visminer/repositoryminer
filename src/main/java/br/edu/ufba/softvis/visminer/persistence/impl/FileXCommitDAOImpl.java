package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.FileXCommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileXCommitPK;
import br.edu.ufba.softvis.visminer.persistence.dao.FileXCommitDAO;

/**
 * Implementation of interface {@link FileXCommitDAO}
 */

public class FileXCommitDAOImpl extends DAOImpl<FileXCommitDB, FileXCommitPK> implements FileXCommitDAO {

	public List<FileXCommitDB> findByCommit(int commitId) {
		
		EntityManager em = getEntityManager();
		TypedQuery<FileXCommitDB> query = em.createNamedQuery("FileXCommitDB.findByCommit", FileXCommitDB.class);
		query.setParameter("commitId", commitId);
		return query.getResultList();
		
	}


}
