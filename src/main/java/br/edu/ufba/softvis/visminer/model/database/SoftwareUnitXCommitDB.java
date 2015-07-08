package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * The persistent class for the software_unit_x_commit database table.
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

	/**
	 * @param id
	 */
	public SoftwareUnitXCommitDB(SoftwareUnitXCommitPK id) {
		super();
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public SoftwareUnitXCommitPK getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(SoftwareUnitXCommitPK id) {
		this.id = id;
	}

	/**
	 * @return the metricValues
	 */
	public List<MetricValueDB> getMetricValues() {
		return metricValues;
	}

	/**
	 * @param metricValues the metricValues to set
	 */
	public void setMetricValues(List<MetricValueDB> metricValues) {
		this.metricValues = metricValues;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SoftwareUnitXCommitDB other = (SoftwareUnitXCommitDB) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}