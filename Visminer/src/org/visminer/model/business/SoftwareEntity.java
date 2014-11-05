package org.visminer.model.business;

import java.util.ArrayList;
import java.util.List;

import org.visminer.persistence.PersistenceFacade;

public class SoftwareEntity {

	private int id;
	private String name;
	private int type;
	private List<Metric> metrics;
	
	public SoftwareEntity(){}
	
	public SoftwareEntity(int id, String name, int type) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public List<Metric> getMetrics() {
		if(this.metrics == null) initMetrics();
		return this.metrics;
	}
	
	public void setMetrics(List<Metric> metrics) {
		this.metrics = metrics;
	}
	
	private void initMetrics(){
		
		PersistenceFacade persistence = new PersistenceFacade();
		List<org.visminer.model.database.MetricValue> metricsValues = persistence.getMetricsValuesBySoftwareEntity(this.id);
		this.metrics = new ArrayList<Metric>(metricsValues.size());
		
		for(org.visminer.model.database.MetricValue metricValue : metricsValues){
			Metric metric = new Metric(metricValue.getMetric().getName(), metricValue.getMetric().getDescription(),
					metricValue.getValue(), metricValue.getMetric().getId());
			this.metrics.add(metric);
		}
		
	}
	
	public Metric getMetric(String name){
		
		if(this.metrics == null) initMetrics();
		
		Metric m = new Metric();
		m.setName(name);
		
		int index = this.metrics.indexOf(m);
		return this.metrics.get(index);
		
	}
	
}
