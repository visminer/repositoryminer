package br.edu.ufba.softvis.visminer.config;

import java.util.List;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.metric.IMetric;
import br.edu.ufba.softvis.visminer.model.database.MetricDB;
import br.edu.ufba.softvis.visminer.persistence.dao.MetricDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.MetricDAOImpl;

public class MetricConfig {

	public static void setMetricsClassPath(List<String> classPaths){
		
		MetricDAO dao = new MetricDAOImpl();
		for (String classPath : classPaths) {
			try {
				IMetric metric = (IMetric) Class.forName(classPath).newInstance();
				saveMetric(metric, dao);
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static void saveMetric(IMetric metric, MetricDAO dao){
		
		MetricAnnotation annotations = metric.getClass().getAnnotation(MetricAnnotation.class);
		
		MetricDB metricDB = dao.getByAcronym(annotations.acronym());
		if(metricDB == null){
			metricDB = new MetricDB();
			metricDB.setName(annotations.name());
			metricDB.setAcronym(annotations.acronym());
			metricDB.setDescription(annotations.description());
			dao.save(metricDB);
		}
		
	}
	
}