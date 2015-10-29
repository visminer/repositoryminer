package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.MetricValueDB;
import br.edu.ufba.softvis.visminer.model.database.MetricValuePK;
import br.edu.ufba.softvis.visminer.persistence.dao.MetricValueDAO;

/**
 * Implementation of interface {@link MetricValueDAO}
 */

public class MetricValueDAOImpl extends DAOImpl<MetricValueDB, MetricValuePK> implements MetricValueDAO{

	public List<MetricValueDB> findBySoftwareUnitAndCommit(int softwareUnitId, int commitId) {
		
		EntityManager em = getEntityManager();
		TypedQuery<MetricValueDB> query = em.createNamedQuery("MetricValueDB.findBySoftwareUnitAndCommit",
				MetricValueDB.class);
		query.setHint("javax.persistence.cache.storeMode", "BYPASS");
		query.setHint("javax.persistence.cache.retrieveMode", "BYPASS");
		query.setParameter("commitId", commitId);
		query.setParameter("softwareUnitId", softwareUnitId);
		List<MetricValueDB> l = query.getResultList();
		return l;
		
	}

}
