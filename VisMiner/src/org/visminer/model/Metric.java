package org.visminer.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the metric database table.
 * 
 */
@Entity
@Table(name="metric")
@NamedQuery(name="Metric.findAll", query="SELECT m FROM Metric m")
public class Metric implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="idmetric", unique=true, nullable=false)
	private int idmetric;

	@Column(name="name", nullable=false, length=6)
	private String name;
	
	@Column(name="description", nullable=false, length=100)
	private String description;	

	//bi-directional many-to-one association to MetricValue
	@OneToMany(mappedBy="metric", fetch=FetchType.LAZY)
	private List<MetricValue> metricValues;

	public Metric() {
	}

	public Metric(int idmetric) {
		super();
		this.idmetric = idmetric;
	}

	public int getIdmetric() {
		return this.idmetric;
	}

	public void setIdmetric(int idmetric) {
		this.idmetric = idmetric;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	

	public List<MetricValue> getMetricValues() {
		return this.metricValues;
	}

	public void setMetricValues(List<MetricValue> metricValues) {
		this.metricValues = metricValues;
	}

	public MetricValue addMetricValue(MetricValue metricValue) {
		getMetricValues().add(metricValue);
		metricValue.setMetric(this);

		return metricValue;
	}

	public MetricValue removeMetricValue(MetricValue metricValue) {
		getMetricValues().remove(metricValue);
		metricValue.setMetric(null);

		return metricValue;
	}

}