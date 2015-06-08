package br.edu.ufba.softvis.visminer.persistence.impl;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.persistence.dao.FileDAO;

public class FileDAOImpl extends DAOImpl<FileDB, Integer> implements FileDAO {

	@Override
	public Integer findIdByCode(String uid) {
		
		EntityManager em = getEntityManager();
		Query query = em.createQuery("select f.id from FileDB f where f.uid = :uid");
		query.setParameter("uid", uid);
		
		try{
			return (Integer) query.getSingleResult();
		}catch(Exception e){
			return 0;
		}
		
	}

}
