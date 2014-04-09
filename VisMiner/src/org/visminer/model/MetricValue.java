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
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idmetric_value", unique=true, nullable=false)
	private int idmetricValue;

	@Column(name="value", nullable=false)
	private int value;

	//bi-directional many-to-one association to Metric
	@ManyToOne
	@JoinColumn(name="metric_idmetric", nullable=false)
	private Metric metric;

	//bi-directional many-to-one association to File
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="file_idfile")
	private File file;

	//bi-directional many-to-one association to Version
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="version_idversion")
	private Version version;

	public MetricValue() {
	}

	public int getIdmetricValue() {
		return this.idmetricValue;
	}

	public void setIdmetricValue(int idmetricValue) {
		this.idmetricValue = idmetricValue;
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

	public Version getVersion() {
		return this.version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

}