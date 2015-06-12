package br.edu.ufba.softvis.visminer.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.constant.MetricId;
import br.edu.ufba.softvis.visminer.metric.IMetric;
import br.edu.ufba.softvis.visminer.model.database.MetricDB;
import br.edu.ufba.softvis.visminer.persistence.Database;
import br.edu.ufba.softvis.visminer.persistence.dao.MetricDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.MetricDAOImpl;

public class MetricConfig {

	private static final String CONFIG_FILE = "/META-INF/metrics.xml";
	
	private static Map<Integer, String> readConfig(){
		
		Map<Integer, String> map = new HashMap<Integer, String>();
		
		try{
			
			InputStream inputStream = MetricConfig.class.getResourceAsStream(CONFIG_FILE);
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(inputStream);
			
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("metric");
			for(int i = 0; i < nList.getLength(); i++){
				
				Element element = (Element) nList.item(i);
				map.put(Integer.parseInt(element.getAttribute("id")), element.getTextContent());
				
			}
			
			return map;
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static void setMetricsClassPath(){
		
		Map<Integer, String> map = readConfig();
		
		EntityManager em = Database.getInstance().getEntityManager();
		MetricDAO dao = new MetricDAOImpl();
		dao.setEntityManager(em);
		
		
		for (String classPath : map.values()) {
			try {
				IMetric metric = (IMetric) Class.forName(classPath).newInstance();
				saveMetric(metric, dao);
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		em.close();
		
	}
	
	private static void saveMetric(IMetric metric, MetricDAO dao){
		
		MetricAnnotation annotations = metric.getClass().getAnnotation(MetricAnnotation.class);

		MetricDB metricDB = dao.find(annotations.id().getId());
		if(metricDB == null){
			metricDB = new MetricDB();
			metricDB.setId(annotations.id());
			metricDB.setName(annotations.name());
			metricDB.setAcronym(annotations.acronym());
			metricDB.setDescription(annotations.description());
			dao.save(metricDB);
		}
		
	}
	
	public static Map<MetricId, IMetric> getImplementations(List<MetricId> metricsId){

		try{
			
			Map<Integer, String> map = readConfig();
			Map<MetricId, IMetric> metrics = new HashMap<MetricId, IMetric>(metricsId.size());
			
			for(MetricId metricId : metricsId){
				String classPath = map.get(metricId.getId());
				IMetric metric = (IMetric) Class.forName(classPath).newInstance();
				metrics.put(metricId, metric);
			}
			return metrics;
			
		}catch(InstantiationException | IllegalAccessException
				| ClassNotFoundException e){
			e.printStackTrace();
			return null;
		}
		
	}
	
}