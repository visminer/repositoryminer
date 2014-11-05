package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.database.Metric;

public class MetricDAO{

	public Metric save(Metric metric){
		
		EntityManager em = Connection.getEntityManager();
		em.getTransaction().begin();
		Metric response = em.merge(metric);
		em.getTransaction().commit();
		em.close();
		return response;
		
	}
	
	public List<Metric> getAll(){
		
		EntityManager em = Connection.getEntityManager();
		TypedQuery<Metric> query = em.createQuery("select m from Metric m", Metric.class);
		
		try{
			List<Metric> result = query.getResultList();
			em.close();
			return result;
		}catch(NoResultException e){
			return null;
		}
		
	}
	
	public Metric getByName(String name){
		
		EntityManager em = Connection.getEntityManager();
		TypedQuery<Metric> query = em.createQuery("select m from Metric m where m.name = :arg0", Metric.class);
		query.setParameter("arg0", name);
		
		try{
			Metric result = query.getSingleResult();
			em.close();
			return result;
		}catch(NoResultException e){
			return null;
		}
		
	}
	
	public Metric get(int id){
		
		EntityManager em = Connection.getEntityManager();
		Metric metric = em.find(Metric.class, id);
		em.close();
		return metric;
		
	}
		
}