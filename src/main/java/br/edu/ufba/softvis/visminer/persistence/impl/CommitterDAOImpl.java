package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.CommitterDB;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitterDAO;

/**
 * Implementation of interface {@link CommitterDAO}
 */

@SuppressWarnings("unchecked")
public class CommitterDAOImpl extends DAOImpl<CommitterDB, Integer>
implements CommitterDAO {

  @Override
  public CommitterDB findByEmail(String email) {

    EntityManager em = getEntityManager();
    TypedQuery<CommitterDB> query = em.createNamedQuery(
        "CommitterDB.findByEmail", CommitterDB.class);
    query.setParameter("email", email);
    return (CommitterDB) getSingleResult(query);

  }

  @Override
  public List<CommitterDB> findByRepository(int repositoryId) {

    EntityManager em = getEntityManager();
    TypedQuery<CommitterDB> query = em.createNamedQuery(
        "CommitterDB.findByRepository", CommitterDB.class);
    query.setHint("javax.persistence.cache.storeMode", "BYPASS");
    query.setHint("javax.persistence.cache.retrieveMode", "BYPASS");
    query.setParameter("repositoryId", repositoryId);
    return getResultList(query);

  }

}