package br.edu.ufba.softvis.visminer.model.bean;

import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;

public class SoftwareUnit {

	private int id;
	private String name;
	private SoftwareUnitType type;
	private SoftwareUnit parentUnit;
	
	public SoftwareUnit(){}
	
	public SoftwareUnit(int id, String name, SoftwareUnitType type) {
		super();
		this.id = id;
		this.name = name;
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

	public SoftwareUnitType getType() {
		return type;
	}

	public void setType(SoftwareUnitType type) {
		this.type = type;
	}

	public SoftwareUnit getParenUnitt() {
		return parentUnit;
	}

	public void setParentUnit(SoftwareUnit parentUnit) {
		this.parentUnit = parentUnit;
	}	
	
}
