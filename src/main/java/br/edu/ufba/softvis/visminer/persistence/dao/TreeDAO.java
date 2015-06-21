package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.TreeDB;

/**
 * 
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * Tree table DAO interface.
 * 
 */
public interface TreeDAO extends DAO<TreeDB, Integer>{

	/**
	 * 
	 * @param id
	 * @return List of trees by repository.
	 */
	public List<TreeDB> findByRepository(int id);

}
