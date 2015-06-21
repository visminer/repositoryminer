package br.edu.ufba.softvis.visminer.persistence.dao;

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
	 * @param uid
	 * @return Repository by uid.
	 */
	public RepositoryDB findByUid(String uid);

}
