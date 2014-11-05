package org.visminer.model.database;

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
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private int id;

	@Column(name="description", length=255, nullable=false)
	private String description;

	@Column(name="name", length=45, nullable=false, unique=true)
	private String name;

	//bi-directional many-to-one association to MetricValue
	@OneToMany(mappedBy="metric")
	private List<MetricValue> metricValues;

	public Metric() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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