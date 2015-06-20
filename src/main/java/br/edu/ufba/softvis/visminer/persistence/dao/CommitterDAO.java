package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.CommitterDB;

/**
 * 
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * Committer table DAO interface.
 * 
 */
public interface CommitterDAO extends DAO<CommitterDB, Integer>{

	/**
	 * 
	 * @param email
	 * @return Committer by email.
	 */
	public CommitterDB findByEmail(String email);

	/**
	 * 
	 * @param repositoryId
	 * @return List of commits by repository.
	 */
	public List<CommitterDB> findByRepository(int repositoryId);
	
}
