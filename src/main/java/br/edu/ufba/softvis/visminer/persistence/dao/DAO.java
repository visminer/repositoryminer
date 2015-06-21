package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.Collection;

import javax.persistence.EntityManager;

/**
 * 
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * Generic DAO interface.
 * 
 */
public interface DAO<E, K> {

	/**
	 * 
	 * @param objects
	 * Save a list of objects.
	 */
	public void batchSave(Collection<E> objects);
	
	/**
	 * 
	 * @param objects
	 * Merge(update or save) a list of objects.
	 */
	public void batchMerge(Collection<E> objects);
	
	/**
	 * 
	 * @param objects
	 * Deletes a list of objects.
	 */
	public void batchDelete(Collection<E> objects);
	
	/**
	 * 
	 * @param object
	 * Save an object.
	 */
	public void save(E object);
	
	/**
	 * 
	 * @param object
	 * @return The object merged.
	 * Merge(update or save) an object.
	 */
	public E merge(E object);
	
	/**
	 * 
	 * @param object
	 * Delete a nobject.
	 */
	public void delete(E object);
	
	/**
	 * 
	 * @param id
	 * @return Finds an object by its id.
	 */
	public E find(K id);
	
	/**
	 * 
	 * @return Finds all objects.
	 */
	public Collection<E> findAll();
	
	/**
	 * 
	 * @param entityManager
	 * Sets database connection inside the DAO.
	 */
	public void setEntityManager(EntityManager entityManager);
	
	/**
	 * 
	 * @return Database connection instance.
	 */
	public EntityManager getEntityManager();
	
}
