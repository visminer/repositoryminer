package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.MetricValueDB;
import br.edu.ufba.softvis.visminer.model.database.MetricValuePK;

public interface MetricValueDAO extends DAO<MetricValueDB, MetricValuePK> {

	public List<MetricValueDB> findBySoftwareUnitAndCommit(int softwareUnitId, int commitId);


}
