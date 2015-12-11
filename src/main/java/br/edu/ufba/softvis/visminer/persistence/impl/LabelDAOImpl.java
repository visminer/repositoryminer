package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.LabelDB;
import br.edu.ufba.softvis.visminer.persistence.dao.LabelDAO;

/**
 * Implementation of interface {@link LabelDAO}
 */
@SuppressWarnings("unchecked")
public class LabelDAOImpl extends DAOImpl<LabelDB, Integer> implements LabelDAO{

  @Override
  public List<LabelDB> findByIssue(int issueId) {

    EntityManager em = getEntityManager();
    TypedQuery<LabelDB> query = em.createNamedQuery(
        "LabelDB.findByIssue", LabelDB.class);
    query.setParameter("id", issueId);
    return getResultList(query);

  }

}
