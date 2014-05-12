package org.visminer.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * <p>
 * The persistent class for the version database table.
 * <b>Note: Version can be a branch or tag</b>
 * </p>
 * 
 * @author Felipe
 * @version 1.0
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
	@JoinColumn(name="repository_idGit", nullable=false)
	private Repository repository;

	public Version() {
	}
	
	/**
	 * @return the idversion
	 */
	public int getIdversion() {
		return idversion;
	}

	/**
	 * @param idversion the idversion to set
	 */
	public void setIdversion(int idversion) {
		this.idversion = idversion;
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
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the commits
	 */
	public List<Commit> getCommits() {
		return commits;
	}

	/**
	 * @param commits the commits to set
	 */
	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}

	/**
	 * @return the metricValues
	 */
	public List<MetricValue> getMetricValues() {
		return metricValues;
	}

	/**
	 * @param metricValues the metricValues to set
	 */
	public void setMetricValues(List<MetricValue> metricValues) {
		this.metricValues = metricValues;
	}

	/**
	 * @return the repository
	 */
	public Repository getRepository() {
		return repository;
	}

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	/**
	 * 
	 * @param metricValue
	 * @return metricValue added
	 */
	public MetricValue addMetricValue(MetricValue metricValue) {
		getMetricValues().add(metricValue);
		metricValue.setVersion(this);

		return metricValue;
	}

	/**
	 * 
	 * @param metricValue
	 * @return metricValue removed
	 */
	public MetricValue removeMetricValue(MetricValue metricValue) {
		getMetricValues().remove(metricValue);
		metricValue.setVersion(null);

		return metricValue;
	}

}