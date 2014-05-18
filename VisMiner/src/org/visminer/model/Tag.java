package org.visminer.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tag database table.
 * 
 */
@Entity
@Table(name="tag")
@NamedQuery(name="Tag.findAll", query="SELECT t FROM Tag t")
public class Tag implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TagPK id;

	//bi-directional many-to-many association to Commit
	@ManyToMany
	@JoinTable(
		name="commit_has_tag"
		, joinColumns={
			@JoinColumn(name="tag_name", referencedColumnName="name"),
			@JoinColumn(name="tag_repository_id_git", referencedColumnName="repository_id_git")
			}
		, inverseJoinColumns={
			@JoinColumn(name="commit_sha")
			}
		)
	private List<Commit> commits;

	//bi-directional many-to-one association to MetricValue
	@OneToMany(mappedBy="tag")
	private List<MetricValue> metricValues;

	//bi-directional many-to-one association to Repository
	@ManyToOne
	private Repository repository;

	public Tag() {
	}

	public TagPK getId() {
		return this.id;
	}

	public void setId(TagPK id) {
		this.id = id;
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
		metricValue.setTag(this);

		return metricValue;
	}

	public MetricValue removeMetricValue(MetricValue metricValue) {
		getMetricValues().remove(metricValue);
		metricValue.setTag(null);

		return metricValue;
	}

	public Repository getRepository() {
		return this.repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

}