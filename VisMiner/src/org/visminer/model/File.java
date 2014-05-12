package org.visminer.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * <p>
 * The persistent class for the file database table.
 * </p>
 * 
 * @author Felipe
 * @version 1.0
 */
@Entity
@Table(name="file")
@NamedQuery(name="File.findAll", query="SELECT f FROM File f")
public class File implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idfile", unique=true, nullable=false)
	private int idfile;

	@Column(name="path", nullable=false, length=400)
	private String path;

	//bi-directional many-to-one association to Commit
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="commit_sha", nullable=false)
	private Commit commit;

	//bi-directional many-to-one association to MetricValue
	@OneToMany(mappedBy="file", fetch=FetchType.EAGER)
	private List<MetricValue> metricValues;

	public File() {
	}
	
	/**
	 * @return the idfile
	 */
	public int getIdfile() {
		return idfile;
	}

	/**
	 * @param idfile the idfile to set
	 */
	public void setIdfile(int idfile) {
		this.idfile = idfile;
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
	 * @return the commit
	 */
	public Commit getCommit() {
		return commit;
	}

	/**
	 * @param commit the commit to set
	 */
	public void setCommit(Commit commit) {
		this.commit = commit;
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
	 * 
	 * @param metricValue
	 * @return metricValue added
	 */
	public MetricValue addMetricValue(MetricValue metricValue) {
		getMetricValues().add(metricValue);
		metricValue.setFile(this);

		return metricValue;
	}

	/**
	 * 
	 * @param metricValue
	 * @return metricValue removed
	 */
	public MetricValue removeMetricValue(MetricValue metricValue) {
		getMetricValues().remove(metricValue);
		metricValue.setFile(null);

		return metricValue;
	}

}