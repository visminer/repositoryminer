
package br.edu.ufba.softvis.visminer.persistence.dao;

import br.edu.ufba.softvis.visminer.model.Repository;

/**
 * Repository table DAO interface.
 */
public interface RepositoryDAO extends DAO<Repository, String> {

	/**
	 * @param repositoryPath
	 * @return true if repository was analyzed
	 */
	public boolean hasRepository(String repositoryPath);

	/**
	 * @param path
	 * @return Repository with given path
	 */
	public Repository findByPath(String path);
	
}
