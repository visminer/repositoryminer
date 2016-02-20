package br.edu.ufba.softvis.visminer.retriever;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.model.SoftwareUnit;
import br.edu.ufba.softvis.visminer.model.database.MetricValueDB;
import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitDB;
import br.edu.ufba.softvis.visminer.persistence.dao.MetricValueDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.SoftwareUnitDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.relational.MetricValueDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.relational.SoftwareUnitDAOImpl;

public class SoftwareUnitRetriever extends Retriever {

	public List<SoftwareUnit> findByFile(int fileId, int commitId) {
		
		List<SoftwareUnit> softwareUnits = new ArrayList<SoftwareUnit>();
		SoftwareUnitDAO dao = new SoftwareUnitDAOImpl();
		
		super.createEntityManager();
		super.shareEntityManager(dao);
		List<SoftwareUnitDB> softUnitsDb = dao.findByFile(fileId);
		if (softUnitsDb != null) {
			softwareUnits = SoftwareUnitDB.toBusiness(softUnitsDb);

			for (SoftwareUnit softUnit : softwareUnits) {
				Map<MetricUid, String> metricsVal = findMetricValue(
						softUnit.getId(), commitId);
				softUnit.setMetricValues(metricsVal);
			}
		}
		
		super.closeEntityManager();
		
		return softwareUnits;
		
	}

	public SoftwareUnit findByRepository(int repositoryId, int commitId) {
		
		List<SoftwareUnit> softwareUnits = new ArrayList<SoftwareUnit>();
		SoftwareUnitDAO dao = new SoftwareUnitDAOImpl();
		
		super.createEntityManager();
		super.shareEntityManager(dao);
		List<SoftwareUnitDB> softUnitsDb = dao.findByRepository(repositoryId, commitId);
		
		if (softUnitsDb != null) {
			softwareUnits = SoftwareUnitDB.toBusiness(softUnitsDb);

			for (SoftwareUnit softUnit : softwareUnits) {
				Map<MetricUid, String> metricsVal = findMetricValue(
						softUnit.getId(), commitId);
				softUnit.setMetricValues(metricsVal);
			}
		}

		processListToTree(softwareUnits);
		return softwareUnits.get(0);
		
	}

	private Map<MetricUid, String> findMetricValue(int softwareUnitId, int commitId) {
		
		MetricValueDAO metricValDao = new MetricValueDAOImpl();
		super.createEntityManager();
		super.shareEntityManager(metricValDao);

		List<MetricValueDB> metricValsDb = metricValDao
				.findBySoftwareUnitAndCommit(softwareUnitId, commitId);
		Map<MetricUid, String> metricVal = new HashMap<MetricUid, String>();

		for (MetricValueDB elem : metricValsDb) {
			metricVal.put(elem.getMetric().getId(), elem.getValue());
		}

		return metricVal;
		
	}

	private void processListToTree(List<SoftwareUnit> softwareUnits) {
		
		int i = 0;
		while (i < softwareUnits.size()) {
			SoftwareUnit elem = softwareUnits.get(i);
			if (elem.getParent() == null) {
				i++;
			} else {
				elem = softwareUnits.remove(i);
				setElementInTree(softwareUnits, elem);
			}
		}
		
	}

	private void setElementInTree(List<SoftwareUnit> list, SoftwareUnit elem) {
		
		if (list == null) {
			return;
		}

		if (list.contains(elem.getParent())) {
			int index = list.indexOf(elem.getParent());
			SoftwareUnit parent = list.get(index);
			if (parent.getChildren() == null) {
				parent.setChildren(new ArrayList<SoftwareUnit>());
			}
			elem.setParent(parent);
			parent.getChildren().add(elem);
		} else {
			for (int i = 0; i < list.size(); i++) {
				setElementInTree(list.get(i).getChildren(), elem);
			}
		}
		
	}

}