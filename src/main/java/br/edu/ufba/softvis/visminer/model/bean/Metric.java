package br.edu.ufba.softvis.visminer.model.bean;

import br.edu.ufba.softvis.visminer.constant.MetricType;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * 
 * Simple metric bean.
 * This bean is used to simplify interaction between some parts, avoiding coupling and doing smaller core codes.
 */

public class Metric {

	private int id;
	private String acronym;
	private String description;
	private String name;
	private MetricType type;
	
	public Metric(){}

	/**
	 * @param id
	 * @param acronym
	 * @param description
	 * @param name
	 * @param type
	 */
	public Metric(int id, String acronym, String description, String name,
			MetricType type) {
		super();
		this.id = id;
		this.acronym = acronym;
		this.description = description;
		this.name = name;
		this.type = type;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
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
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(MetricType type) {
		this.type = type;
	}
	
}