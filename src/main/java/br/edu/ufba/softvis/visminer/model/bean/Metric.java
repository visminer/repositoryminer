package br.edu.ufba.softvis.visminer.model.bean;

public class Metric {

	private int id;
	private String acronym;
	private String description;
	private String name;
	
	public Metric(){}
	
	public Metric(int id, String acronym, String description, String name) {
		super();
		this.id = id;
		this.acronym = acronym;
		this.description = description;
		this.name = name;
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
	
}
