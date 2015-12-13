package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.MetricValueDB;
import br.edu.ufba.softvis.visminer.model.database.MetricValuePK;
import br.edu.ufba.softvis.visminer.persistence.dao.MetricValueDAO;

/**
 * Implementation of interface {@link MetricValueDAO}
 */
@SuppressWarnings("unchecked")
public class MetricValueDAOImpl extends DAOImpl<MetricValueDB, MetricValuePK> 
implements MetricValueDAO{

  @Override
  public List<MetricValueDB> findByCommitAndTypeAndSoftwareUnits(int commitId,
      int type, List<Integer> softUnitIds) {

    EntityManager em = getEntityManager();
    TypedQuery<MetricValueDB> query = em.createNamedQuery(
        "MetricValueDB.findByCommitAndType", MetricValueDB.class);
    query.setParameter("commitId", commitId);
    query.setParameter("type", type);
    query.setParameter("softUnitIds", softUnitIds);
    return getResultList(query);

  }

}