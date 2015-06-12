
package br.edu.ufba.softvis.visminer.persistence.impl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.MetricDB;
import br.edu.ufba.softvis.visminer.persistence.dao.MetricDAO;

public class MetricDAOImpl extends DAOImpl<MetricDB, Integer> implements MetricDAO{

	@Override
	public MetricDB findByAcronym(String acronym) {
		
		EntityManager em = getEntityManager();
		TypedQuery<MetricDB> query = em.createNamedQuery("MetricDB.findByAcronym", MetricDB.class);
		query.setParameter("acronym", acronym);
		
		try{
			return query.getSingleResult();
		}catch(Exception e){
			return null;
		}
		
	}

}
