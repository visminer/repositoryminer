package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;

/**
 * 
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * Repository table DAO interface.
 * 
 */
public interface RepositoryDAO extends DAO<RepositoryDB, Integer> {

	/**
	 * 
	 */
	public List<RepositoryDB> findAll();
	
	/**
	 * 
	 * @param uid
	 * @return Repository by uid.
	 */
	public RepositoryDB findByUid(String uid);

}
