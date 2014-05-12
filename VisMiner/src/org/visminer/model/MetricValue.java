package org.visminer.model;

import java.io.Serializable;

import javax.persistence.*;


/**
 * <p>
 * The persistent class for the metric_value database table.
 * </p>
 * 
 * @author Felipe
 * @version 1.0
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

	/**
	 * @return the idmetricValue
	 */
	public int getIdmetricValue() {
		return idmetricValue;
	}

	/**
	 * @param idmetricValue the idmetricValue to set
	 */
	public void setIdmetricValue(int idmetricValue) {
		this.idmetricValue = idmetricValue;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the metric
	 */
	public Metric getMetric() {
		return metric;
	}

	/**
	 * @param metric the metric to set
	 */
	public void setMetric(Metric metric) {
		this.metric = metric;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return the version
	 */
	public Version getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Version version) {
		this.version = version;
	}

}