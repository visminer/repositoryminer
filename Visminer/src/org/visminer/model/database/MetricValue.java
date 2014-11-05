package org.visminer.model.database;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the metric_value database table.
 * 
 */
@Entity
@Table(name="metric_value")
@NamedQuery(name="MetricValue.findAll", query="SELECT m FROM MetricValue m")
public class MetricValue implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MetricValuePK id;

	@Column(name="value", length=45, nullable=false)
	private String value;

	//bi-directional many-to-one association to Metric
	@ManyToOne
	private Metric metric;

	//bi-directional many-to-one association to SoftwareEntity
	@ManyToOne
	@JoinColumn(name="software_entity_id")
	private SoftwareEntity softwareEntity;

	public MetricValue() {
	}

	public MetricValuePK getId() {
		return this.id;
	}

	public void setId(MetricValuePK id) {
		this.id = id;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Metric getMetric() {
		return this.metric;
	}

	public void setMetric(Metric metric) {
		this.metric = metric;
	}

	public SoftwareEntity getSoftwareEntity() {
		return this.softwareEntity;
	}

	public void setSoftwareEntity(SoftwareEntity softwareEntity) {
		this.softwareEntity = softwareEntity;
	}

}