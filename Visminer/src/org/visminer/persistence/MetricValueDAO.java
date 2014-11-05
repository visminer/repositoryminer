package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.database.MetricValue;

public class MetricValueDAO{

	public void saveAll(List<MetricValue> metricValues){
		
		EntityManager em = Connection.getEntityManager();
		em.getTransaction().begin();
		for(int i = 0; i < metricValues.size(); i++){
			em.merge(metricValues.get(i)).getId();
		}
		em.getTransaction().commit();
		em.close();
		
	}
	
	public List<MetricValue> getBySoftwareEntity(int softwareEntityId){
		
		EntityManager em = Connection.getEntityManager();
		TypedQuery<MetricValue> query = em.createQuery("select mv from MetricValue mv join mv.metric m where mv.softwareEntity.id = :arg0", MetricValue.class);
		query.setParameter("arg0", softwareEntityId);
		
		try{
			List<MetricValue> resp = query.getResultList();
			em.close();
			return resp;
		}catch(NoResultException e){
			em.close();
			return null;
		}
		
	}
	
}
