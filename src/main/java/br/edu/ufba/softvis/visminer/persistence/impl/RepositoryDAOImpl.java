package br.edu.ufba.softvis.visminer.persistence.impl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.persistence.dao.RepositoryDAO;
import br.edu.ufba.softvis.visminer.utility.StringUtils;

/**
 * Implementation of interface {@link RepositoryDAO}
 */
public class RepositoryDAOImpl extends DAOImpl<RepositoryDB, Integer> 
implements RepositoryDAO{

  @Override
  public RepositoryDB findByUid(String uid) {

    EntityManager em = getEntityManager();
    TypedQuery<RepositoryDB> query = em.createNamedQuery(
        "RepositoryDB.findByUid", RepositoryDB.class);
    query.setParameter("uid", uid);
    return (RepositoryDB) getSingleResult(query);

  }

  @Override
  public boolean hasRepository(String repositoryPath) {

    String uid = StringUtils.sha1(repositoryPath.replace("\\", "/"));
    EntityManager em = getEntityManager();
    TypedQuery<Long> query = em.createNamedQuery(
        "RepositoryDB.countByUid", Long.class);
    query.setParameter("uid", uid);
    return ( (Long) getSingleResult(query) != 0);

  }

  @Override
  public RepositoryDB findByPath(String path){
    String uid = StringUtils.sha1(path.replace("\\", "/"));
    return findByUid(uid);
  }

}
