package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.Metric;

/**
 * <p>
 * The operation class for the metric database table.
 * </p>
 * @author Felipe
 * @version 1.0
 */
public class MetricDAO{

	private Connection connection = Connection.getInstance();
	
	/**
	 * <p>
	 * saves the metric in database if id is set otherwise updates the metric in database
	 * </p>
	 * @param metric
	 * @return metric saved
	 */
	public Metric save(Metric metric){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		metric = em.merge(metric);
		em.getTransaction().commit();
		em.close();
		return metric;
		
	}
	
	/**
	 * 
	 * @param idmetric
	 * @return metric by id
	 */
	public Metric getOne(int idmetric){
		
		EntityManager em = connection.getEntityManager();
		TypedQuery<Metric> query = em.createQuery("SELECT m FROM Metric m WHERE m.idmetric = :arg1", Metric.class);
		query.setParameter("arg1", idmetric);
		
		try{
			return query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
	/**
	 * 
	 * @return all metrics
	 */
	public List<Metric> getAll(){
		
		EntityManager em = connection.getEntityManager();
		TypedQuery<Metric> query = em.createQuery("SELECT m FROM Metric m", Metric.class);
		
		try{
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
		
	}
	
}
