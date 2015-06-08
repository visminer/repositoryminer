package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the metric_value database table.
 * 
 */
@Embeddable
public class MetricValuePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="software_unit_id", insertable=true, updatable=false, nullable=false)
	private int softwareUnitId;

	@Column(name="commit_id", insertable=true, updatable=false, nullable=false)
	private int commitId;

	@Column(name="metric_id", insertable=true, updatable=false, nullable=false)
	private int metricId;

	public MetricValuePK() {
	}
	public int getSoftwareUnitId() {
		return this.softwareUnitId;
	}
	public void setSoftwareUnitId(int softwareUnitId) {
		this.softwareUnitId = softwareUnitId;
	}
	public int getCommitId() {
		return this.commitId;
	}
	public void setCommitId(int commitId) {
		this.commitId = commitId;
	}
	public int getMetricId() {
		return this.metricId;
	}
	public void setMetricId(int metricId) {
		this.metricId = metricId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MetricValuePK)) {
			return false;
		}
		MetricValuePK castOther = (MetricValuePK)other;
		return 
			(this.softwareUnitId == castOther.softwareUnitId)
			&& (this.commitId == castOther.commitId)
			&& (this.metricId == castOther.metricId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.softwareUnitId;
		hash = hash * prime + this.commitId;
		hash = hash * prime + this.metricId;
		
		return hash;
	}
}