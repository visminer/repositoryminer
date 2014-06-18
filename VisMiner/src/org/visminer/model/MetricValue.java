package org.visminer.model;

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

	@Id
	@Column(name="id_metric_value")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idMetricValue;

	@Column(name="value", nullable=false)
	private int value;

	//bi-directional many-to-one association to Metric
	@ManyToOne
	@JoinColumn(name = "metric_id_metric", referencedColumnName = "id_metric")
	private Metric metric;

	//bi-directional many-to-one association to File
	@ManyToOne
	@JoinColumn(name = "file_id_file", referencedColumnName = "id_file")
	private File file;

	//bi-directional many-to-one association to Tag
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="tag_name", referencedColumnName="name"),
		@JoinColumn(name="tag_repository_id_git", referencedColumnName="repository_id_git")
		})
	private Tag tag;

	public MetricValue() {
	}

	public int getIdMetricValue() {
		return this.idMetricValue;
	}

	public void setIdMetricValue(int idMetricValue) {
		this.idMetricValue = idMetricValue;
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Metric getMetric() {
		return this.metric;
	}

	public void setMetric(Metric metric) {
		this.metric = metric;
	}

	public File getFile() {
		return this.file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Tag getTag() {
		return this.tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

}