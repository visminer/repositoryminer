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
import br.edu.ufba.softvis.visminer.constant.MetricType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.metric.IMetric;
import br.edu.ufba.softvis.visminer.model.database.MetricDB;
import br.edu.ufba.softvis.visminer.persistence.Database;
import br.edu.ufba.softvis.visminer.persistence.dao.MetricDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.MetricDAOImpl;

/**
 * @version 0.9
 * Manages metrics.
 */
public class MetricConfig {

	private static final String CONFIG_FILE = "/META-INF/metrics.xml";
	
	// Reads the configuration file e return its data.
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
				String uid = element.getAttribute("id").toUpperCase();
				map.put(MetricUid.valueOf(uid).getId(), element.getTextContent());
				
			}
			
			return map;
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * Loads the classes marked in configuration file and use reflections to load the metrics class
	 * Saves the informations about the metrics
	 */
	public static void setMetricsClassPath(){
		
		Map<Integer, String> map = readConfig();
		
		EntityManager em = Database.getInstance().getEntityManager();
		MetricDAO dao = new MetricDAOImpl();
		dao.setEntityManager(em);
		
		
		for (String classPath : map.values()) {
				IMetric metric;
				try {
					metric = (IMetric) Class.forName(classPath).newInstance();
					saveMetric(metric, dao);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
		}
		
		em.close();
		
	}
	
	// Save metric in database 
	private static void saveMetric(IMetric metric, MetricDAO dao){
		
		MetricAnnotation annotations = metric.getClass().getAnnotation(MetricAnnotation.class);

		MetricDB metricDB = dao.find(annotations.uid().getId());
		if(metricDB == null){
			metricDB = new MetricDB(annotations.uid(), annotations.acronym(),
					annotations.description(), annotations.name(), annotations.type());
			dao.save(metricDB);
		}
		
	}
	
	/**
	 * @param metricsId
	 * @return Returns a map containing the metric id and metric implementation
	 * Finds the implementation for the metrics id passed as parameter and return a map, with the pair, metric id and its implementation.
	 */
	public static Map<MetricType, Map<MetricUid, IMetric>> getImplementations(List<MetricUid> metricsId){

		Map<MetricType, Map<MetricUid, IMetric>> metricMap = new HashMap<MetricType, Map<MetricUid,IMetric>>();
		
		Map<Integer, String> map = readConfig();
		
		Map<MetricUid, IMetric> simpleMetrics = new HashMap<MetricUid, IMetric>();
		Map<MetricUid, IMetric> complexMetrics = new HashMap<MetricUid, IMetric>();
		
		try{
			
			for(MetricUid metricUid : metricsId){
				String classPath = map.get(metricUid.getId());
				IMetric metric = (IMetric) Class.forName(classPath).newInstance();
				MetricAnnotation annotations = metric.getClass().getAnnotation(MetricAnnotation.class);
				
				if(annotations.type().equals(MetricType.SIMPLE)){
					simpleMetrics.put(metricUid, metric);
				}else if(annotations.type().equals(MetricType.COMPLEX)){
					complexMetrics.put(metricUid, metric);
				}
			}
			
			metricMap.put(MetricType.SIMPLE, simpleMetrics);
			metricMap.put(MetricType.COMPLEX, complexMetrics);
			
			return metricMap;
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
}