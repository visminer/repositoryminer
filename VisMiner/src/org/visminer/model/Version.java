package org.visminer.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the version database table.
 * 
 */
@Entity
@Table(name="version")
@NamedQuery(name="Version.findAll", query="SELECT v FROM Version v")
public class Version implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idversion", unique=true, nullable=false)
	private int idversion;

	@Column(name="name", nullable=false, length=45)
	private String name;

	@Column(name="path", nullable=false, length=100)
	private String path;

	@Column(name="type", nullable=false, length=6)
	private String type;

	//bi-directional many-to-many association to Commit
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
		name="commit_version"
		, joinColumns={
			@JoinColumn(name="version_idversion", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="commit_sha", nullable=false)
			}
		)
	private List<Commit> commits;

	//bi-directional many-to-one association to MetricValue
	@OneToMany(mappedBy="version", fetch=FetchType.EAGER)
	private List<MetricValue> metricValues;

	//bi-directional many-to-one association to Repository
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="repository_idrepository", nullable=false)
	private Repository repository;

	public Version() {
	}

	public int getIdversion() {
		return this.idversion;
	}

	public void setIdversion(int idversion) {
		this.idversion = idversion;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Commit> getCommits() {
		return this.commits;
	}

	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}

	public List<MetricValue> getMetricValues() {
		return this.metricValues;
	}

	public void setMetricValues(List<MetricValue> metricValues) {
		this.metricValues = metricValues;
	}

	public MetricValue addMetricValue(MetricValue metricValue) {
		getMetricValues().add(metricValue);
		metricValue.setVersion(this);

		return metricValue;
	}

	public MetricValue removeMetricValue(MetricValue metricValue) {
		getMetricValues().remove(metricValue);
		metricValue.setVersion(null);

		return metricValue;
	}

	public Repository getRepository() {
		return this.repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

}