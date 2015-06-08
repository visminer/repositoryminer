
package br.edu.ufba.softvis.visminer.persistence.impl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.MetricDB;
import br.edu.ufba.softvis.visminer.persistence.dao.MetricDAO;

public class MetricDAOImpl extends DAOImpl<MetricDB, Integer> implements MetricDAO{

	@Override
	public MetricDB getByAcronym(String acronym) {
		
		EntityManager em = getEntityManager();
		TypedQuery<MetricDB> query = em.createQuery("select m from MetricDB m where m.acronym = :acronym", MetricDB.class);
		query.setParameter("acronym", acronym);
		
		try{
			return query.getSingleResult();
		}catch(Exception e){
			return null;
		}
		
	}

}
