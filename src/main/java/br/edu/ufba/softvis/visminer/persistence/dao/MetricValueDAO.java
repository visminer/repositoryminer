package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.MetricValueDB;
import br.edu.ufba.softvis.visminer.model.database.MetricValuePK;

/**
 * Metric_value table DAO interface.
 */
public interface MetricValueDAO extends DAO<MetricValueDB, MetricValuePK> {

  /**
   * Returns values of all calculated metrics in certain commit and 
   * of a certain type(SNAPSHOT or COMMIT).
   * @param commitId
   * @param type
   * @return
   */
  public List<MetricValueDB> findByCommitAndTypeAndSoftwareUnits(int commitId,
      int type, List<Integer> softUnitIds);

}
