package org.visminer.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the file database table.
 * 
 */
@Entity
@Table(name="file")
@NamedQuery(name="File.findAll", query="SELECT f FROM File f")
public class File implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_file")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idFile;

	@Column(name="path", nullable=false, length=400)
	private String path;

	//bi-directional many-to-one association to Commit
	@ManyToOne
	@JoinColumn(name = "commit_sha", referencedColumnName = "sha")
	private Commit commit;

	//bi-directional many-to-one association to MetricValue
	@OneToMany(mappedBy="file")
	private List<MetricValue> metricValues;

	public File() {
	}

	public int getIdFile() {
		return this.idFile;
	}

	public void setIdFile(int idFile) {
		this.idFile = idFile;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Commit getCommit() {
		return this.commit;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
	}

	public List<MetricValue> getMetricValues() {
		return this.metricValues;
	}

	public void setMetricValues(List<MetricValue> metricValues) {
		this.metricValues = metricValues;
	}

	public MetricValue addMetricValue(MetricValue metricValue) {
		getMetricValues().add(metricValue);
		metricValue.setFile(this);

		return metricValue;
	}

	public MetricValue removeMetricValue(MetricValue metricValue) {
		getMetricValues().remove(metricValue);
		metricValue.setFile(null);

		return metricValue;
	}

}