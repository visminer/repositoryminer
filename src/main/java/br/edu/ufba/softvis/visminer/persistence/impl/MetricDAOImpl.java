package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.List;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.metric.IMetric;
import br.edu.ufba.softvis.visminer.persistence.Database;
import br.edu.ufba.softvis.visminer.persistence.dao.MetricDAO;

public class MetricDAOImpl implements MetricDAO {

	@Override
	public void save(Document document) {
		Database.getInstance().insert("types", document);
	}

	@Override
	public List<IMetric> findByFile(String uid) {
		return null;
	}

}
