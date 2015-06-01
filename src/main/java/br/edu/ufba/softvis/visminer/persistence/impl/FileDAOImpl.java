package br.edu.ufba.softvis.visminer.persistence.impl;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.edu.ufba.softvis.visminer.model.FileDB;
import br.edu.ufba.softvis.visminer.persistence.dao.FileDAO;

public class FileDAOImpl extends DAOImpl<FileDB, Integer> implements FileDAO {

	@Override
	public Integer findIdByCode(String code) {
		
		EntityManager em = getEntityManager();
		Query query = em.createQuery("select f.id from File f where f.code = :code");
		query.setParameter("code", code);
		
		try{
			return (Integer) query.getSingleResult();
		}catch(Exception e){
			return 0;
		}
		
	}

}
