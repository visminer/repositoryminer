package br.edu.ufba.softvis.visminer.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.model.business.Commit;
import br.edu.ufba.softvis.visminer.model.business.SoftwareUnit;
import br.edu.ufba.softvis.visminer.model.database.MetricValueDB;
import br.edu.ufba.softvis.visminer.model.database.MetricValuePK;
import br.edu.ufba.softvis.visminer.persistence.dao.MetricValueDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.SoftwareUnitDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.MetricValueDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.SoftwareUnitDAOImpl;

/**
 * Persistence interface for metrics calculation.
 */
public class MetricPersistance {

	
	private MetricValueDAO metricValueDao;

	private int commitId;
	private MetricUid metric;
	private SoftwareUnit projectUnit;
	
	private List<MetricValueDB> metricValues;
	
	public MetricPersistance(EntityManager entityManager, String repositoryUid) {

		this.metricValues  = new ArrayList<MetricValueDB>();

		this.metricValueDao = new MetricValueDAOImpl();
		this.metricValueDao.setEntityManager(entityManager);
		
		SoftwareUnitDAO softUnitDAO = new SoftwareUnitDAOImpl();
		softUnitDAO.setEntityManager(entityManager);
		
		projectUnit = softUnitDAO.findByUid(repositoryUid).toBusiness();

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
	
	public void postMetricValue(int softwareUnitId, String value){
		
		MetricValuePK metricValPk = new MetricValuePK(softwareUnitId, commitId, metric.getId());
		MetricValueDB metricValDb =  new MetricValueDB(metricValPk, value);
		metricValues.add(metricValDb);
		
	}
	
	public void flushAllMetricValues() {
		metricValueDao.batchMerge(metricValues);
		metricValues.clear();
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

	public SoftwareUnit getProject(){
		return projectUnit;
	}
	
}