package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Generic DAO interface.
 */
public interface DAO<E, K> {

  /**
   * @param objects
   * Save a list of objects.
   */
  public void batchSave(List<E> objects);

  /**
   * @param objects
   * Merge(update or save) a list of objects.
   */
  public void batchMerge(List<E> objects);

  /**
   * @param objects
   * Deletes a list of objects.
   */
  public void batchDelete(List<E> objects);

  /**
   * @param ids
   * Deletes a list of objects by their ids.
   */
  public void batchDelete2(List<K> ids);

  /**
   * @param object
   * Save an object.
   */
  public void save(E object);

  /**
   * @param object
   * @return The object merged.
   * Merge(update or save) an object.
   */
  public E merge(E object);

  /**
   * @param object
   * Delete an object.
   */
  public void delete(E object);

  /**
   * @param object
   * Delete an object by its id.
   */
  public void delete2(K id);

  /**
   * @param id
   * @return Finds an object by its id.
   */
  public E find(K id);

  /**
   * @return Finds all objects.
   */
  public List<E> findAll();

  /**
   * 
   * @param query
   * @return The result set
   */
  @SuppressWarnings("rawtypes")
  public List getResultList(Query query);

  /**
   * @param query
   * @return Return the result of a query
   * This method is used to avoid code repeating, when getSingleResult doesn't
   * have nothing to return an exception is thrown, this method is a template
   * method to avoid repeat try...catch block. 
   */
  public Object getSingleResult(Query query);

  /**
   * @param entityManager
   * Sets database connection inside the DAO.
   */
  public void setEntityManager(EntityManager entityManager);

  /**
   * @return Database connection instance.
   */
  public EntityManager getEntityManager();

}