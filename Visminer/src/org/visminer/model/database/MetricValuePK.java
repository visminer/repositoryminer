package org.visminer.model.database;

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

	@Column(name="software_entity_id", insertable=false, updatable=false)
	private int softwareEntityId;

	@Column(name="metric_id", insertable=false, updatable=false)
	private int metricId;

	public MetricValuePK() {
	}
	public int getSoftwareEntityId() {
		return this.softwareEntityId;
	}
	public void setSoftwareEntityId(int softwareEntityId) {
		this.softwareEntityId = softwareEntityId;
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
			(this.softwareEntityId == castOther.softwareEntityId)
			&& (this.metricId == castOther.metricId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.softwareEntityId;
		hash = hash * prime + this.metricId;
		
		return hash;
	}
}