package org.visminer.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * <p>
 * The persistent class for the metric database table.
 * </p>
 * 
 * @author Felipe
 * @version 1.0
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

	/**
	 * @return the idmetric
	 */
	public int getIdmetric() {
		return idmetric;
	}

	/**
	 * @param idmetric the idmetric to set
	 */
	public void setIdmetric(int idmetric) {
		this.idmetric = idmetric;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the metricValues
	 */
	public List<MetricValue> getMetricValues() {
		return metricValues;
	}

	/**
	 * @param metricValues the metricValues to set
	 */
	public void setMetricValues(List<MetricValue> metricValues) {
		this.metricValues = metricValues;
	}

	/**
	 * 
	 * @param metricValue
	 * @return metricValue added
	 */
	public MetricValue addMetricValue(MetricValue metricValue) {
		getMetricValues().add(metricValue);
		metricValue.setMetric(this);

		return metricValue;
	}

	/**
	 * 
	 * @param metricValue
	 * @return metricValue removed
	 */
	public MetricValue removeMetricValue(MetricValue metricValue) {
		getMetricValues().remove(metricValue);
		metricValue.setMetric(null);

		return metricValue;
	}

}