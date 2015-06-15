package br.edu.ufba.softvis.visminer.model.business;

import java.util.List;
import java.util.Map;

import br.edu.ufba.softvis.visminer.constant.MetricId;
import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;

public class SoftwareUnit {

	private int id;
	private String name;
	private String fullName;
	private String uid;
	private SoftwareUnitType type;
	private SoftwareUnit parentUnit;
	private List<SoftwareUnit> children;
	private Map<MetricId, String> metricValues;
	private File file;
	
	public SoftwareUnit(){}

	public SoftwareUnit(int id, String name, String fullName, String uid,
			SoftwareUnitType type) {
		super();
		this.id = id;
		this.name = name;
		this.fullName = fullName;
		this.uid = uid;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public SoftwareUnitType getType() {
		return type;
	}

	public void setType(SoftwareUnitType type) {
		this.type = type;
	}

	public SoftwareUnit getParentUnit() {
		return parentUnit;
	}

	public void setParentUnit(SoftwareUnit parentUnit) {
		this.parentUnit = parentUnit;
	}

	public Map<MetricId, String> getMetricValues() {
		return metricValues;
	}

	public void setMetricValues(Map<MetricId, String> metricValues) {
		this.metricValues = metricValues;
	}

	public List<SoftwareUnit> getChildren() {
		return children;
	}

	public void setChildren(List<SoftwareUnit> children) {
		this.children = children;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		SoftwareUnit other = (SoftwareUnit) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}