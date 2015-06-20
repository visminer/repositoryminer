package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.MetricValueDB;
import br.edu.ufba.softvis.visminer.model.database.MetricValuePK;

/**
 * 
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * Metric_value table DAO interface.
 * 
 */
public interface MetricValueDAO extends DAO<MetricValueDB, MetricValuePK> {

	/**
	 * 
	 * @param softwareUnitId
	 * @param commitId
	 * @return List of metrics values by software unit and commit.
	 */
	public List<MetricValueDB> findBySoftwareUnitAndCommit(int softwareUnitId, int commitId);


}
