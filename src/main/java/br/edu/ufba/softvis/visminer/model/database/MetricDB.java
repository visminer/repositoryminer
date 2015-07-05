package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;

import javax.persistence.*;

import br.edu.ufba.softvis.visminer.constant.MetricType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;

import java.util.List;


/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * The persistent class for the metric database table.
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

	@Column(nullable=false)
	@Lob
	private String description;

	@Column(nullable=false, length=100)
	private String name;
	
	@Column(name="type", nullable=false)
	private int type;

	//bi-directional many-to-one association to MetricValueDB
	@OneToMany(mappedBy="metric")
	private List<MetricValueDB> metricValues;

	public MetricDB() {
	}

	/**
	 * @param id
	 * @param acronym
	 * @param description
	 * @param name
	 * @param type
	 */
	public MetricDB(MetricUid id, String acronym, String description, String name,
			MetricType type) {
		super();
		this.id = id.getId();
		this.acronym = acronym;
		this.description = description;
		this.name = name;
		this.type = type.getId();
	}

	/**
	 * @return the id
	 */
	public MetricUid getId() {
		return MetricUid.parse(id);
	}

	/**
	 * @param id the id to set
	 */
	public void setId(MetricUid id) {
		this.id = id.getId();
	}

	/**
	 * @return the acronym
	 */
	public String getAcronym() {
		return acronym;
	}

	/**
	 * @param acronym the acronym to set
	 */
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public MetricType getType() {
		return MetricType.parse(type);
	}

	/**
	 * @param type the type to set
	 */
	public void setType(MetricType type) {
		this.type = type.getId();
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


}