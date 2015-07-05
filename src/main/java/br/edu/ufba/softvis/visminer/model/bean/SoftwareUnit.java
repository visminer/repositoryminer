package br.edu.ufba.softvis.visminer.model.bean;

import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;

/**
 * @version 0.9
 * Simple software unit bean.
 * This bean is used to simplify interaction between some parts, avoiding coupling and doing smaller core codes.
 */

public class SoftwareUnit {

	private int id;
	private String name;
	private String uid;
	private SoftwareUnitType type;
	private SoftwareUnit parentUnit;
	
	public SoftwareUnit(){}

	/**
	 * @param id
	 * @param name
	 * @param uid
	 * @param type
	 */
	public SoftwareUnit(int id, String name, String uid,
			SoftwareUnitType type) {
		super();
		this.id = id;
		this.name = name;
		this.uid = uid;
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
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the type
	 */
	public SoftwareUnitType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(SoftwareUnitType type) {
		this.type = type;
	}

	/**
	 * @return the parentUnit
	 */
	public SoftwareUnit getParentUnit() {
		return parentUnit;
	}

	/**
	 * @param parentUnit the parentUnit to set
	 */
	public void setParentUnit(SoftwareUnit parentUnit) {
		this.parentUnit = parentUnit;
	}

}