
package br.edu.ufba.softvis.visminer.persistence.dao;

import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;

/**
 * Repository table DAO interface.
 */
public interface RepositoryDAO extends DAO<RepositoryDB, Integer> {

  /**
   * @param uid
   * @return Repository with given uid.
   */
  public RepositoryDB findByUid(String uid);

  /**
   * @param repositoryPath
   * @return True if repository was analyzed and False otherwise.
   */
  public boolean hasRepository(String repositoryPath);

  /**
   * @param path
   * @return Repository with given path.
   */
  public RepositoryDB findByPath(String path);

}
