package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.CommitterDB;

/**
 * Committer table DAO interface.
 */
public interface CommitterDAO extends DAO<CommitterDB, Integer>{

  /**
   * @param email
   * @return Committer by email.
   */
  public CommitterDB findByEmail(String email);

  /**
   * @param repositoryId
   * @return List of commits by repository.
   */
  public List<CommitterDB> findByRepository(int repositoryId);

}
