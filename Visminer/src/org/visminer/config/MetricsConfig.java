package org.visminer.config;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.visminer.metric.IMetric;
import org.visminer.model.business.Metric;
import org.visminer.persistence.PersistenceFacade;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MetricsConfig {

	private static final String PACKAGE = "org.visminer.metric."; 
	
	private static final String CONFIG_PARAM_TAG_NAME = "metric";
	private static final String CONFIG_PARAM_ON = "on";
	private static final String CONFIG_PARAM_FILTER_EXTENSION = "filter_extension";
	private static final String CONFIG_PARAM_FILTER_MIME_TYPE = "filter-mime-type";
	private static final String CONFIG_PARAM_SEPARATOR = " ";
	
	public static Map<Metric, MetricAttribute> metrics = new HashMap<Metric, MetricAttribute>();
	
	
	public static void configure(String path){
		
		try{
			
			File file = new File(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName(CONFIG_PARAM_TAG_NAME);
			
			for(int i = 0; i < nList.getLength(); i++){
				Node node = nList.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE){
					Element element = (Element) node;
					getData(element);
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private static void getData(Element element){
		
		boolean on = Boolean.parseBoolean(element.getAttribute(CONFIG_PARAM_ON));
		if(!on) return;
		
		try {
			
			String clsName = element.getTextContent();
			IMetric<?> metricCls = (IMetric<?>) Class.forName(PACKAGE+clsName).newInstance();
			
			String exts = element.getAttribute(CONFIG_PARAM_FILTER_EXTENSION);
			List<String> extensions = Arrays.asList(exts.split(CONFIG_PARAM_SEPARATOR));
			
			String mimes = element.getAttribute(CONFIG_PARAM_FILTER_MIME_TYPE);
			List<String> mimeTypes = Arrays.asList(mimes.split(CONFIG_PARAM_SEPARATOR));
			
			MetricAttribute metricAttr = new MetricAttribute(metricCls, extensions, mimeTypes);
			Metric metric = getMetric(metricCls);
			
			metrics.put(metric, metricAttr);
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
	
	private static Metric getMetric(IMetric<?> metricCls){
		
		PersistenceFacade persistence = new PersistenceFacade();
		
		org.visminer.model.database.Metric metricDb = persistence.getMetricByName(metricCls.getName());
		if(metricDb != null){
			return new Metric(metricDb.getName(), metricDb.getDescription(), metricDb.getId());
		}
		
		metricDb = new org.visminer.model.database.Metric();
		metricDb.setDescription(metricCls.getDescription());
		metricDb.setName(metricCls.getName());
		
		org.visminer.model.database.Metric metricDb2 = persistence.saveMetric(metricDb);
		return new Metric(metricDb2.getName(), metricDb2.getDescription(), metricDb2.getId());
		
	}
	
}
