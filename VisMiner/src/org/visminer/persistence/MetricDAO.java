package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.Metric;

public class MetricDAO{

	private Connection connection = Connection.getInstance();
	
	public Metric save(Metric metric){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		metric = em.merge(metric);
		em.getTransaction().commit();
		em.close();
		return metric;
		
	}
	
	public Metric getOne(int idmetric){
		
		EntityManager em = connection.getEntityManager();
		TypedQuery<Metric> query = em.createQuery("SELECT m FROM Metric m WHERE m.idMetric = :arg1", Metric.class);
		query.setParameter("arg1", idmetric);
		
		try{
			return query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
	public List<Metric> getAll(){
		
		EntityManager em = connection.getEntityManager();
		TypedQuery<Metric> query = em.createQuery("SELECT m FROM Metric m", Metric.class);
		
		try{
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
		
	}
	
	public Metric getByName(String name){

		EntityManager em = connection.getEntityManager();
		TypedQuery<Metric> query = em.createQuery("SELECT m FROM Metric m WHERE m.name = :arg1", Metric.class);
		query.setParameter("arg1", name);
		
		try{
			return query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}
	
}
