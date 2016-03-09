package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.Commit;

/**
 * Commit table DAO interface.
 */
public interface CommitDAO extends DAO<Commit, String> {

	/**
	 * @param uid
	 * @return List of commits by tree.
	 */
	public List<Commit> findByTree(String uid);

	/**
	 * @param uid
	 * @return All commits from a given repository.
	 */
	public List<Commit> findByRepository(String uid);

}
