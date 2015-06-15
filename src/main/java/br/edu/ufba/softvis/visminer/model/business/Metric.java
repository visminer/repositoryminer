package br.edu.ufba.softvis.visminer.model.business;

import br.edu.ufba.softvis.visminer.constant.MetricId;

public class Metric {

	private int id;
	private String acronym;
	private String description;
	private String name;
	private MetricId uid;
	
	public Metric(){}
	
	public Metric(int id, String acronym, String description, String name,
			MetricId uid) {
		super();
		this.id = id;
		this.acronym = acronym;
		this.description = description;
		this.name = name;
		this.uid = uid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MetricId getUid() {
		return uid;
	}

	public void setUid(MetricId uid) {
		this.uid = uid;
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
		Metric other = (Metric) obj;
		if (id != other.id)
			return false;
		return true;
	}

	
	
}
