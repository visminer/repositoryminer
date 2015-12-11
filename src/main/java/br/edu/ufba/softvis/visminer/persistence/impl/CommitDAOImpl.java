package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitDAO;
import br.edu.ufba.softvis.visminer.utility.StringUtils;

/**
 * Implementation of interface {@link CommitDAO}
 */
@SuppressWarnings("unchecked")
public class CommitDAOImpl extends DAOImpl<CommitDB, Integer> 
implements CommitDAO{

  @Override
  public List<CommitDB> findByTree(int treeId) {

    EntityManager em = getEntityManager();
    TypedQuery<CommitDB> query = em.createNamedQuery("CommitDB.findByTree",
        CommitDB.class);
    query.setParameter("id", treeId);
    return getResultList(query);

  }

  @Override
  public List<CommitDB> findByRepository(String repositoryPath) {

    String uid = StringUtils.sha1(repositoryPath.replace("\\", "/"));

    EntityManager em = getEntityManager();
    TypedQuery<CommitDB> query = em.createNamedQuery(
        "CommitDB.findByRepository", CommitDB.class);
    query.setParameter("uid", uid);
    return getResultList(query);

  }

  @Override
  public List<CommitDB> findNotRefIssue(int repoId) {

    EntityManager em = getEntityManager();
    TypedQuery<CommitDB> query = em.createNamedQuery(
        "CommitDB.findNotRefIssue", CommitDB.class);
    query.setParameter("repoId", repoId);
    return getResultList(query);

  }

  @Override
  public List<Integer> findIdsUntilDate(int repositoryId, Date until) {

    EntityManager em = getEntityManager();
    Query query = em.createNamedQuery("CommitDB.findIdsUntilDate");
    query.setParameter("until", until, TemporalType.TIMESTAMP);
    query.setParameter("repositoryId", repositoryId);

    List<Integer> list = query.getResultList();
    if(list == null)
      list = new ArrayList<Integer>();

    return list;
  }

}