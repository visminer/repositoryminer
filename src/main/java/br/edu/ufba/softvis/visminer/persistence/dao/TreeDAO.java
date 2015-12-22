package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.Tree;

/**
 * 
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9 Tree table DAO interface.
 * 
 */
public interface TreeDAO extends DAO<Tree, String> {

	/**
	 * @param uid
	 * @return List of trees by repository.
	 */
	public List<Tree> findByRepository(String uid);

}
