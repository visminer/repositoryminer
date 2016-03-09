package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.Committer;

/**
 * 
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * Committer table DAO interface.
 * 
 */
public interface CommitterDAO extends DAO<Committer, Integer>{

	/**
	 * 
	 * @param email
	 * @return Committer by email.
	 */
	public Committer findByEmail(String email);

	/**
	 * 
	 * @param repositoryId
	 * @return List of commits by repository.
	 */
	public List<Committer> findByRepository(int repositoryId);
	
}
