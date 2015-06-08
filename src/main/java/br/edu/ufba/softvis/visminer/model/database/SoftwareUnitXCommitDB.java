package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the software_unit_x_commit database table.
 * 
 */
@Entity
@Table(name="software_unit_x_commit")
@NamedQuery(name="SoftwareUnitXCommitDB.findAll", query="SELECT s FROM SoftwareUnitXCommitDB s")
public class SoftwareUnitXCommitDB implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SoftwareUnitXCommitPK id;

	//bi-directional many-to-one association to MetricValueDB
	@OneToMany(mappedBy="softwareUnitXCommit")
	private List<MetricValueDB> metricValues;

	public SoftwareUnitXCommitDB() {
	}

	public SoftwareUnitXCommitPK getId() {
		return this.id;
	}

	public void setId(SoftwareUnitXCommitPK id) {
		this.id = id;
	}

	public List<MetricValueDB> getMetricValues() {
		return this.metricValues;
	}

	public void setMetricValues(List<MetricValueDB> metricValues) {
		this.metricValues = metricValues;
	}

	public MetricValueDB addMetricValue(MetricValueDB metricValue) {
		getMetricValues().add(metricValue);
		metricValue.setSoftwareUnitXCommit(this);

		return metricValue;
	}

	public MetricValueDB removeMetricValue(MetricValueDB metricValue) {
		getMetricValues().remove(metricValue);
		metricValue.setSoftwareUnitXCommit(null);

		return metricValue;
	}

}