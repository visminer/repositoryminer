package br.edu.ufba.softvis.visminer.persistence.impl;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.edu.ufba.softvis.visminer.persistence.dao.DAO;

public class DAOImpl<E, K> implements DAO<E, K>{

	private EntityManager entityManager;
	Class entityClass;
	
	public DAOImpl(){
		ParameterizedType genericClass = (ParameterizedType) getClass().getGenericSuperclass();
		entityClass = (Class) genericClass.getActualTypeArguments()[0];
	}
	
	@Override
	public void batchSave(Collection<E> objects) {

		entityManager.getTransaction().begin();
		int i = 0;
		for(E object : objects){
			entityManager.persist(object);
			if((i % 1000) == 0){
				entityManager.getTransaction().commit();
				entityManager.clear();
				entityManager.getTransaction().begin();
			}
			i++;
		}
		entityManager.getTransaction().commit();
		
	}

	@Override
	public void batchMerge(Collection<E> objects) {

		entityManager.getTransaction().begin();
		int i = 0;
		for(E object : objects){			
			entityManager.merge(object);
			if((i % 1000) == 0){
				entityManager.getTransaction().commit();
				entityManager.clear();
				entityManager.getTransaction().begin();
			}
			i++;
		}
		
		entityManager.getTransaction().commit();
		
	}

	@Override
	public void batchDelete(Collection<E> objects) {
		
		entityManager.getTransaction().begin();
		int i = 0;
		for(E object : objects){			
			entityManager.remove(object);
			if((i % 1000) == 0){
				entityManager.getTransaction().commit();
				entityManager.clear();
				entityManager.getTransaction().begin();
			}
			i++;
		}
		
		entityManager.getTransaction().commit();
		
	}

	@Override
	public void save(E object) {
		entityManager.getTransaction().begin();
		entityManager.persist(object);
		entityManager.getTransaction().commit();		
	}

	@Override
	public E merge(E object) {
		entityManager.getTransaction().begin();
		E o = entityManager.merge(object);
		entityManager.getTransaction().commit();
		return o;
	}

	@Override
	public void delete(E object) {
		entityManager.getTransaction().begin();
		entityManager.remove(object);
		entityManager.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	@Override
	public E find(K id) {
		try{
			return (E) entityManager.find(entityClass, id);
		}catch(NoResultException e){
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<E> findAll() {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<E> q = cb.createQuery(entityClass);
		Root<E> r = q.from(entityClass);
		q.select(r);
		TypedQuery<E> query = entityManager.createQuery(q);
		return query.getResultList();
		
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

}