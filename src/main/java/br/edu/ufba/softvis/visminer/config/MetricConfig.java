package br.edu.ufba.softvis.visminer.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

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
import br.edu.ufba.softvis.visminer.utility.XMLUtil;

/**
 * @version 0.9
 * Manages metrics.
 */
public class MetricConfig {

  private static Map<Integer, String> configMap;
  private static final String MAPPING_FILE = "/META-INF/metrics.xml";

  // Reads the metrics configuration and return metrics canonical names.
  private static Map<Integer, String> readConfig(){

    Map<Integer, String> map = new HashMap<Integer, String>();
    Document doc = XMLUtil.getXMLDoc(MAPPING_FILE);
    NodeList nList = doc.getElementsByTagName("metric");

    for(int i = 0; i < nList.getLength(); i++){
      Element element = (Element) nList.item(i);
      String uid = element.getAttribute("id").toUpperCase();
      map.put(MetricUid.valueOf(uid).getId(), element.getTextContent());
    }

    return map;

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
   * Finds the implementation for the metrics id passed as parameter and return a map, with the 
   * pair, metric id and its implementation.
   */
  public static void getImplementations(List<MetricUid> metricsId, Map<MetricUid, IMetric> 
  commitMetrics, Map<MetricUid, IMetric> snapshotMetrics){

    configMap = readConfig();
    for(MetricUid uid : metricsId){
      String canonicalName = configMap.get(uid.getId());
      IMetric metric = getIMetricInstance(canonicalName);
      addMetric(metric, commitMetrics, snapshotMetrics);
    }

  }

  // Get an IMetric instance from a class canonical name
  private static IMetric getIMetricInstance(String canonicalName){

    try {
      IMetric metric = (IMetric) Class.forName(canonicalName).newInstance();
      return metric;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }

  // Adds the metric in one of the lists based on metric type
  private static void addMetric(IMetric metric, Map<MetricUid, IMetric> commitMetrics,
      Map<MetricUid, IMetric> snapshotMetrics){

    MetricAnnotation annotations = metric.getClass().getAnnotation(MetricAnnotation.class);

    for(MetricUid uid : annotations.requisites()){
      String canonicalName = configMap.get(uid.getId());
      IMetric metric2 = getIMetricInstance(canonicalName);
      addMetric(metric2, commitMetrics, snapshotMetrics);
    }

    Map<MetricUid, IMetric> auxList = null;

    if(annotations.type().equals(MetricType.COMMIT)){
      auxList = commitMetrics;
    }else if(annotations.type().equals(MetricType.SNAPSHOT)){
      auxList = snapshotMetrics;
    }

    if(!auxList.containsKey(annotations.uid())){
      auxList.put(annotations.uid(), metric);
    }

  }

}