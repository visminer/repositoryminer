package br.edu.ufba.softvis.visminer.retriever;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.model.business.SoftwareUnit;
import br.edu.ufba.softvis.visminer.model.database.MetricValueDB;
import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitDB;
import br.edu.ufba.softvis.visminer.persistence.dao.MetricValueDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.SoftwareUnitDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.MetricValueDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.SoftwareUnitDAOImpl;

public class SoftwareUnitRetriever extends Retriever {

	public List<SoftwareUnit> findByFile(int fileId, int commitId) {
		List<SoftwareUnit> softwareUnits = new ArrayList<SoftwareUnit>();

		SoftwareUnitDAO dao = super.newDAO(SoftwareUnitDAOImpl.class);

		List<SoftwareUnitDB> softUnitsDb = dao.findByFile(fileId);
		if (softUnitsDb != null) {
			softwareUnits = SoftwareUnitDB.toBusiness(softUnitsDb);

			for (SoftwareUnit softUnit : softwareUnits) {
				Map<MetricUid, String> metricsVal = findMetricValue(
						softUnit.getId(), commitId);
				softUnit.setMetricValues(metricsVal);
			}
		}

		super.closeDAO(dao);
		
		return softwareUnits;
	}

	public List<SoftwareUnit> findByRepository(int repositoryId, int commitId) {
		List<SoftwareUnit> softwareUnits = new ArrayList<SoftwareUnit>();

		SoftwareUnitDAO dao = super.newDAO(SoftwareUnitDAOImpl.class);

		List<SoftwareUnitDB> softUnitsDb = dao.findByRepository(repositoryId);
		if (softUnitsDb != null) {
			softwareUnits = SoftwareUnitDB.toBusiness(softUnitsDb);

			for (SoftwareUnit softUnit : softwareUnits) {
				Map<MetricUid, String> metricsVal = findMetricValue(
						softUnit.getId(), commitId);
				softUnit.setMetricValues(metricsVal);
			}
		}

		processListToTree(softwareUnits);

		super.closeDAO(dao);
		
		return softwareUnits;
	}

	public Map<MetricUid, String> findMetricValue(int softwareUnitId,
			int commitId) {
		MetricValueDAO metricValDao = new MetricValueDAOImpl();
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
			if (elem.getSoftwareUnit() == null) {
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

		if (list.contains(elem.getSoftwareUnit())) {
			int index = list.indexOf(elem.getSoftwareUnit());
			SoftwareUnit parent = list.get(index);
			if (parent.getChildren() == null) {
				parent.setChildren(new ArrayList<SoftwareUnit>());
			}
			elem.setSoftwareUnit(parent);
			parent.getChildren().add(elem);
		} else {
			for (int i = 0; i < list.size(); i++) {
				setElementInTree(list.get(i).getChildren(), elem);
			}
		}
	}

}
