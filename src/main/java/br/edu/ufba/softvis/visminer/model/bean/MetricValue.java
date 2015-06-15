package br.edu.ufba.softvis.visminer.model.bean;

public class MetricValue {

	private String metricAcronym;
	private String value;
	
	public MetricValue(){}
	
	public MetricValue(String metricAcronym, String value) {
		super();
		this.metricAcronym = metricAcronym;
		this.value = value;
	}

	public String getMetricAcronym() {
		return metricAcronym;
	}

	public void setMetricAcronym(String metricAcronym) {
		this.metricAcronym = metricAcronym;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
