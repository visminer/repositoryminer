package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

/**
 * Generic DAO interface.
 */
public interface DAO<E, K> {
	
	/**
	 * @param object
	 * Save an object.
	 */
	public void save(E object);
	
	/**
	 * @param id
	 * @return Finds an object by its id.
	 */
	public E find(K id);
	
	/**
	 * @return Finds all objects.
	 */
	public List<E> findAll();
	
}