package br.edu.ufba.softvis.visminer.model.bean;

import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;
import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitDB;

public class SoftwareUnit {

	private int id;
	private String name;
	private String fullName;
	private String uid;
	private SoftwareUnitType type;
	private SoftwareUnit parentUnit;
	
	public SoftwareUnit(){}

	public SoftwareUnit(SoftwareUnitDB softUnitDb) {
		this.id = softUnitDb.getId();
		this.name = softUnitDb.getName();
		this.fullName = softUnitDb.getFullName();
		this.uid = softUnitDb.getUid();
		this.type = softUnitDb.getType();
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
	
}