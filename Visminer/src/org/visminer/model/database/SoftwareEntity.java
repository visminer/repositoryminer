package org.visminer.model.database;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the software_entity database table.
 * 
 */
@Entity
@Table(name="software_entity")
@NamedQuery(name="SoftwareEntity.findAll", query="SELECT s FROM SoftwareEntity s")
public class SoftwareEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private int id;

	@Column(name="name", nullable=false, length=255)
	private String name;

	@Column(name="type", nullable=false, length=2)
	private int type;

	//bi-directional many-to-one association to MetricValue
	@OneToMany(mappedBy="softwareEntity")
	private List<MetricValue> metricValues;

	//bi-directional many-to-one association to FileXCommit
	@ManyToOne(optional=false)
	@JoinColumns({
		@JoinColumn(name="commit_id", referencedColumnName="commit_id", nullable=false),
		@JoinColumn(name="file_id", referencedColumnName="file_id", nullable=false)
		})
	private FileXCommit fileXCommit;

	//bi-directional many-to-one association to SoftwareEntity
	@ManyToOne
	@JoinColumn(name="parent")
	private SoftwareEntity softwareEntity;

	//bi-directional many-to-one association to SoftwareEntity
	@OneToMany(mappedBy="softwareEntity")
	private List<SoftwareEntity> softwareEntities;

	public SoftwareEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<MetricValue> getMetricValues() {
		return this.metricValues;
	}

	public void setMetricValues(List<MetricValue> metricValues) {
		this.metricValues = metricValues;
	}

	public MetricValue addMetricValue(MetricValue metricValue) {
		getMetricValues().add(metricValue);
		metricValue.setSoftwareEntity(this);

		return metricValue;
	}

	public MetricValue removeMetricValue(MetricValue metricValue) {
		getMetricValues().remove(metricValue);
		metricValue.setSoftwareEntity(null);

		return metricValue;
	}

	public FileXCommit getFileXCommit() {
		return this.fileXCommit;
	}

	public void setFileXCommit(FileXCommit fileXCommit) {
		this.fileXCommit = fileXCommit;
	}

	public SoftwareEntity getSoftwareEntity() {
		return this.softwareEntity;
	}

	public void setSoftwareEntity(SoftwareEntity softwareEntity) {
		this.softwareEntity = softwareEntity;
	}

	public List<SoftwareEntity> getSoftwareEntities() {
		return this.softwareEntities;
	}

	public void setSoftwareEntities(List<SoftwareEntity> softwareEntities) {
		this.softwareEntities = softwareEntities;
	}

	public SoftwareEntity addSoftwareEntity(SoftwareEntity softwareEntity) {
		getSoftwareEntities().add(softwareEntity);
		softwareEntity.setSoftwareEntity(this);

		return softwareEntity;
	}

	public SoftwareEntity removeSoftwareEntity(SoftwareEntity softwareEntity) {
		getSoftwareEntities().remove(softwareEntity);
		softwareEntity.setSoftwareEntity(null);

		return softwareEntity;
	}

}