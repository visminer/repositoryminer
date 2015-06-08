package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the metric_value database table.
 * 
 */
@Entity
@Table(name="metric_value")
@NamedQuery(name="MetricValueDB.findAll", query="SELECT m FROM MetricValueDB m")
public class MetricValueDB implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MetricValuePK id;

	@Lob
	@Column(nullable=false)
	private String value;

	//bi-directional many-to-one association to SoftwareUnitXCommitDB
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="commit_id", referencedColumnName="commit_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="software_unit_id", referencedColumnName="software_unit_id", nullable=false, insertable=false, updatable=false)
		})
	private SoftwareUnitXCommitDB softwareUnitXCommit;

	//bi-directional many-to-one association to MetricDB
	@ManyToOne
	@JoinColumn(name="metric_id", nullable=false, insertable=false, updatable=false)
	private MetricDB metric;

	public MetricValueDB() {
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

	public SoftwareUnitXCommitDB getSoftwareUnitXCommit() {
		return this.softwareUnitXCommit;
	}

	public void setSoftwareUnitXCommit(SoftwareUnitXCommitDB softwareUnitXCommit) {
		this.softwareUnitXCommit = softwareUnitXCommit;
	}

	public MetricDB getMetric() {
		return this.metric;
	}

	public void setMetric(MetricDB metric) {
		this.metric = metric;
	}

}