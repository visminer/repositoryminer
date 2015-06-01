
package br.edu.ufba.softvis.visminer.persistence.impl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.MetricDB;
import br.edu.ufba.softvis.visminer.persistence.dao.MetricDAO;

public class MetricDAOImpl extends DAOImpl<MetricDB, Integer> implements MetricDAO{

	@Override
	public MetricDB findByName(String name) {
		
		EntityManager em = getEntityManager();
		TypedQuery<MetricDB> query = em.createQuery("select m from Metric m where m.name = :names", MetricDB.class);
		query.setParameter("name", name);
		
		try{
			return query.getSingleResult();
		}catch(Exception e){
			return null;
		}
		
	}

}
