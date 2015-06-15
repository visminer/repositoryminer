package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.Collection;

import javax.persistence.EntityManager;

public interface DAO<E, K> {

	public void batchSave(Collection<E> objects);
	public void batchMerge(Collection<E> objects);
	public void batchDelete(Collection<E> objects);
	public void save(E object);
	public E merge(E object);
	public void delete(E object);
	public E find(K id);
	public Collection<E> findAll();
	public void setEntityManager(EntityManager entityManager);
	public EntityManager getEntityManager();
	
}
