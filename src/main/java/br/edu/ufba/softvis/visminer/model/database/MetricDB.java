package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;

import javax.persistence.*;

import br.edu.ufba.softvis.visminer.constant.MetricId;

import java.util.List;


/**
 * The persistent class for the metric database table.
 * 
 */
@Entity
@Table(name="metric")
@NamedQuery(name="MetricDB.findByAcronym", query="select m from MetricDB m where m.acronym = :acronym")

public class MetricDB implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	@Column(nullable=false, length=20)
	private String acronym;

	@Column(nullable=false, length=256)
	private String description;

	@Column(nullable=false, length=100)
	private String name;

	//bi-directional many-to-one association to MetricValueDB
	@OneToMany(mappedBy="metric")
	private List<MetricValueDB> metricValues;

	public MetricDB() {
	}

	public MetricId getId() {
		return MetricId.parse(this.id);
	}

	public void setId(MetricId id) {
		this.id = id.getId();
	}

	public String getAcronym() {
		return this.acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MetricValueDB> getMetricValues() {
		return this.metricValues;
	}

	public void setMetricValues(List<MetricValueDB> metricValues) {
		this.metricValues = metricValues;
	}

	public MetricValueDB addMetricValue(MetricValueDB metricValue) {
		getMetricValues().add(metricValue);
		metricValue.setMetric(this);

		return metricValue;
	}

	public MetricValueDB removeMetricValue(MetricValueDB metricValue) {
		getMetricValues().remove(metricValue);
		metricValue.setMetric(null);

		return metricValue;
	}

}