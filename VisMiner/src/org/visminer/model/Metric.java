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
	@Column(name="id_metric")
	private int idMetric;

	@Column(name="description", nullable=false, length=255)
	private String description;

	@Column(name="name", nullable=false, length=45)
	private String name;

	//bi-directional many-to-one association to MetricValue
	@OneToMany(mappedBy="metric")
	private List<MetricValue> metricValues;

	public Metric() {
	}

	public Metric(int idMetric) {
		super();
		this.idMetric = idMetric;
	}

	public int getIdMetric() {
		return this.idMetric;
	}

	public void setIdMetric(int idMetric) {
		this.idMetric = idMetric;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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