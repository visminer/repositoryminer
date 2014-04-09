package org.visminer.persistence;

import javax.persistence.EntityManager;

import org.visminer.model.MetricValue;

public class MetricValueDAO{

	private Connection connection = Connection.getInstance();
	
	public MetricValue save(MetricValue metricValue){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		metricValue = em.merge(metricValue);
		em.getTransaction().commit();
		em.close();
		return metricValue;
		
	}	
	
}
