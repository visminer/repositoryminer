package br.edu.ufba.softvis.visminer.retriever;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufba.softvis.visminer.model.Metric;
import br.edu.ufba.softvis.visminer.model.database.MetricDB;
import br.edu.ufba.softvis.visminer.persistence.dao.MetricDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.relational.MetricDAOImpl;

public class MetricRetriever extends Retriever{

	public List<Metric> findAll(){
		
		List<Metric> metrics = new ArrayList<Metric>();
		MetricDAO dao = new MetricDAOImpl();
		
		super.createEntityManager();
		super.shareEntityManager(dao);
		
		List<MetricDB> metricsDB = (List<MetricDB>) dao.findAll();
		if(metricsDB != null){
			metrics.addAll(MetricDB.toBusiness(metricsDB));
		}
		
		super.closeEntityManager();
		return metrics;
	}
	
}
