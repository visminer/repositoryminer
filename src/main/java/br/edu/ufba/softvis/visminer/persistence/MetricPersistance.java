package br.edu.ufba.softvis.visminer.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.model.business.Commit;
import br.edu.ufba.softvis.visminer.model.database.MetricValueDB;
import br.edu.ufba.softvis.visminer.model.database.MetricValuePK;
import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitXCommitDB;
import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitXCommitPK;
import br.edu.ufba.softvis.visminer.persistence.dao.MetricValueDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.SoftwareUnitXCommitDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.MetricValueDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.SoftwareUnitXCommitImpl;

/**
 * @version 0.9
 * Persistence interface for metrics calculation.
 */
public class MetricPersistance {

	
	private SoftwareUnitXCommitDAO softUnitXCommitDao;
	private MetricValueDAO metricValueDao;

	private int commitId;
	private MetricUid metric;
	
	private List<SoftwareUnitXCommitDB> unitsXCommits;
	private List<MetricValueDB> metricValues;
	
	public MetricPersistance(EntityManager entityManager) {

		this.unitsXCommits = new ArrayList<SoftwareUnitXCommitDB>();
		this.metricValues  = new ArrayList<MetricValueDB>();

		this.softUnitXCommitDao = new SoftwareUnitXCommitImpl();
		this.softUnitXCommitDao.setEntityManager(entityManager);

		this.metricValueDao = new MetricValueDAOImpl();
		this.metricValueDao.setEntityManager(entityManager);

	}

	/**
	 * @param commit
	 * Sets the current commit of metric calculation.
	 */
	public void setCommitId(int commitId){
		this.commitId = commitId;
	}
	
	/**
	 * @param metricUid
	 * Sets the current metric.
	 */
	public void setMetric(MetricUid metric){
		this.metric = metric;
	}
	
	public void initBatchPersistence() {
		unitsXCommits.clear();
		metricValues.clear();
	}
	
	public void postMetricValue(int softwareUnitId, String value){
		
		SoftwareUnitXCommitPK unitXCommitPk = new SoftwareUnitXCommitPK(softwareUnitId, commitId);
		SoftwareUnitXCommitDB unitXCommit = new SoftwareUnitXCommitDB(unitXCommitPk);
		
		if(!unitsXCommits.contains(unitXCommit)){
			unitsXCommits.add(unitXCommit);
		}
		
		MetricValuePK metricValPk = new MetricValuePK(softwareUnitId, commitId, metric.getId());
		MetricValueDB metricValDb =  new MetricValueDB(metricValPk, value);
		
		metricValues.add(metricValDb);
		
	}
	
	public void flushAllMetricValues() {
		softUnitXCommitDao.batchMerge(unitsXCommits);
		metricValueDao.batchMerge(metricValues);
	}

	/**
	 * @param metricUid
	 * @param softUnitId
	 * @param commitPrev
	 * @return The value of a certain metric calculated over software unit.
	 * Pass commitPrev as parameter if you want get the value of the metric for the software unit in the previous commit.
	 * If you want the value of the metric calculated for the software unit in the current commit pass null as parameter.
	 */
	public String getMetricValue(MetricUid metricUid, int id, Commit commit){

		MetricValuePK pk = new MetricValuePK(id, commit.getId(), metricUid.getId());
		MetricValueDB metricValDb = new MetricValueDB(pk, null);
		
		int index = metricValues.indexOf(metricValDb);
		if(index > -1){
			metricValDb = metricValues.get(index);
		}else{
			metricValDb = metricValueDao.find(pk);
		}

		if(metricValDb == null){
			return null;
		}

		return metricValDb.getValue();

	}

}