package br.edu.ufba.softvis.visminer.persistence.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.edu.ufba.softvis.visminer.persistence.dao.DAO;

/**
 * Implementation of interface {@link DAO}
 */

public class DAOImpl<E, K> implements DAO<E, K>{

	private EntityManager entityManager;
	
	@SuppressWarnings("rawtypes")
	Class entityClass;
	
	@SuppressWarnings("rawtypes")
	public DAOImpl(){
		ParameterizedType genericClass = (ParameterizedType) getClass().getGenericSuperclass();
		entityClass = (Class) genericClass.getActualTypeArguments()[0];
	}
	
	public void batchSave(List<E> objects) {

		EntityTransaction ts = entityManager.getTransaction();
		ts.begin();
		
		for(int i = 0; i < objects.size(); i++){
			
			entityManager.persist(objects.get(i));
			
			if((i % 1000) == 0){
				ts.commit();
				entityManager.clear();
				ts.begin();
			}
			
		}
		
		ts.commit();
		entityManager.clear();
		
	}

	@SuppressWarnings("unchecked")
	public void batchMerge(List<E> objects) {

		EntityTransaction ts = entityManager.getTransaction();
		ts.begin();
		
		for(int i = 0; i < objects.size(); i++){
			
			Object o = objects.get(i);
			objects.set(i, (E) entityManager.merge(o));
			
			if((i % 1000) == 0){
				ts.commit();
				entityManager.clear();
				ts.begin();
			}
			
		}
		
		ts.commit();
		entityManager.clear();
		
	}

	public void batchDelete(List<E> objects) {
		
		EntityTransaction ts = entityManager.getTransaction();
		ts.begin();
		
		for(int i = 0; i < objects.size(); i++){
			
			entityManager.remove(objects.get(i));
			
			if((i % 1000) == 0){
				ts.commit();
				entityManager.clear();
				ts.begin();
			}
			
		}
		
		ts.commit();
		entityManager.clear();
		
	}

	@SuppressWarnings("unchecked")
	public void batchDelete2(List<K> ids){
		
		EntityTransaction ts = entityManager.getTransaction();
		ts.begin();
		
		for(int i = 0; i < ids.size(); i++){
			
			Object o = entityManager.getReference(entityClass, ids.get(i));
			entityManager.remove(o);
			
			if((i % 1000) == 0){
				ts.commit();
				entityManager.clear();
				ts.begin();
			}
			
		}
		
		ts.commit();
		entityManager.clear();
		
	}
	
	public void save(E object) {
		
		EntityTransaction ts = entityManager.getTransaction();
		ts.begin();
		entityManager.persist(object);
		ts.commit();
		entityManager.clear();
		
	}

	public E merge(E object) {
		
		EntityTransaction ts = entityManager.getTransaction();
		ts.begin();
		E o = entityManager.merge(object);
		ts.commit();
		entityManager.clear();
		return o;
		
	}

	public void delete(E object) {
		
		EntityTransaction ts = entityManager.getTransaction();
		ts.begin();
		entityManager.remove(object);
		ts.commit();
		entityManager.clear();
		
	}

	@SuppressWarnings("unchecked")
	public void delete2(K id){
		
		EntityTransaction ts = entityManager.getTransaction();
		ts.begin();
		Object o = entityManager.getReference(entityClass, id);
		entityManager.remove(o);
		ts.commit();
		entityManager.clear();
		
	}
	
	@SuppressWarnings("unchecked")
	public E find(K id) {
		
		try{
			return (E) entityManager.find(entityClass, id);
		}catch(NoResultException e){
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<E> findAll() {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<E> q = cb.createQuery(entityClass);
		Root<E> r = q.from(entityClass);
		q.select(r);
		TypedQuery<E> query = entityManager.createQuery(q);
		return query.getResultList();
		
	}

	public Object getSingleResult(Query query){
		
		try{
			return query.getSingleResult();
		} catch(NoResultException e){
			return null;
		}
		
	}
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

}