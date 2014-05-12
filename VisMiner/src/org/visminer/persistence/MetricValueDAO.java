package org.visminer.persistence;

import javax.persistence.EntityManager;

import org.visminer.model.MetricValue;

/**
 * <p>
 * The operation class for the metric_value database table.
 * </p>
 * @author Felipe
 * @version 1.0
 */
public class MetricValueDAO{

	private Connection connection = Connection.getInstance();
	
	/**
	 * <p>
	 * saves the metric value in database if id is set otherwise updates the metric value in database
	 * </p>
	 * @param metric value
	 * @return metric Value saved
	 */
	public MetricValue save(MetricValue metricValue){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		metricValue = em.merge(metricValue);
		em.getTransaction().commit();
		em.close();
		return metricValue;
		
	}	
	
}
