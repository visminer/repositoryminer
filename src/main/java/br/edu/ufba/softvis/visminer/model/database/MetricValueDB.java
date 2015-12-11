package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the metric_value database table.
 */
@Entity
@Table(name="metric_value")
@NamedQueries({
	@NamedQuery(name="MetricValueDB.findByCommitAndType", query="select mv from"
			+ " MetricValueDB mv where mv.metric.type = :type and "
			+ "mv.softwareUnitXCommit.id.commitId = :commitId and "
			+ "mv.softwareUnitXCommit.id.softwareUnitId in :softUnitIds")
})
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
		@JoinColumn(name="commit_id", referencedColumnName="commit_id",
				nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="software_unit_id", 
				referencedColumnName="software_unit_id", nullable=false, 
				insertable=false, updatable=false)
	})
	private SoftwareUnitXCommitDB softwareUnitXCommit;

	//bi-directional many-to-one association to MetricDB
	@JoinColumn(name="metric_id", nullable=false, insertable=false, 
			updatable=false)
	private MetricDB metric;

	public MetricValueDB() {
	}

	/**
	 * @param id
	 * @param value
	 */
	public MetricValueDB(MetricValuePK id, String value) {
		super();
		this.id = id;
		this.value = value;
	}

	/**
	 * @return the id
	 */
	public MetricValuePK getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(MetricValuePK id) {
		this.id = id;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the softwareUnitXCommit
	 */
	public SoftwareUnitXCommitDB getSoftwareUnitXCommit() {
		return softwareUnitXCommit;
	}

	/**
	 * @param softwareUnitXCommit the softwareUnitXCommit to set
	 */
	public void setSoftwareUnitXCommit(SoftwareUnitXCommitDB 
			softwareUnitXCommit) {
		this.softwareUnitXCommit = softwareUnitXCommit;
	}

	/**
	 * @return the metric
	 */
	public MetricDB getMetric() {
		return metric;
	}

	/**
	 * @param metric the metric to set
	 */
	public void setMetric(MetricDB metric) {
		this.metric = metric;
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
		MetricValueDB other = (MetricValueDB) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}