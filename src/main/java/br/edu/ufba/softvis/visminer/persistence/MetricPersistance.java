package br.edu.ufba.softvis.visminer.persistence;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.model.bean.Commit;
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
	
	private EntityManager entityManager;

	Set<SoftwareUnitXCommitDB> unitsXCommits;
	
	private SoftwareUnitXCommitDAO softUnitXCommitDao;
	private MetricValueDAO metricValueDao;

	private Commit commit;
	private MetricUid metric;
	
	public MetricPersistance(EntityManager entityManager) {

		this.unitsXCommits = new HashSet<SoftwareUnitXCommitDB>();
		this.entityManager = entityManager;

		this.softUnitXCommitDao = new SoftwareUnitXCommitImpl();
		this.softUnitXCommitDao.setEntityManager(entityManager);

		this.metricValueDao = new MetricValueDAOImpl();
		this.metricValueDao.setEntityManager(entityManager);

	}

	/**
	 * @param metric
	 * Sets the metric that is being calculated at time.
	 */
	public void setMetric(MetricUid metric) {
		this.metric = metric;
	}

	/**
	 * @param commit
	 * Sets the current commit of metric calculation.
	 */
	public void setCommit(Commit commit){
		this.commit = commit;
	}
	
	public void saveMetricValue(int id, String value){
		
		SoftwareUnitXCommitPK unitXCommitPk = new SoftwareUnitXCommitPK(id, commit.getId());
		SoftwareUnitXCommitDB unitXCommit = new SoftwareUnitXCommitDB(unitXCommitPk);
		
		if(!unitsXCommits.contains(unitXCommit)){
			if(softUnitXCommitDao.find(unitXCommitPk) == null){
				softUnitXCommitDao.save(unitXCommit);
			}
			unitsXCommits.add(unitXCommit);
		}
		
		MetricValuePK metricValPk = new MetricValuePK(id, commit.getId(), metric.getId());
		MetricValueDB metricValDb =  new MetricValueDB(metricValPk, value);
		metricValueDao.merge(metricValDb);
		
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
		MetricValueDB metricValDb = metricValueDao.find(pk);

		if(metricValDb == null){
			return null;
		}

		return metricValDb.getValue();

	}

}