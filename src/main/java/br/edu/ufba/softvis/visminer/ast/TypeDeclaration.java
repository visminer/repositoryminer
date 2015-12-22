package br.edu.ufba.softvis.visminer.ast;

import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;

public class TypeDeclaration {

	private String uid;
	private String name;
	private SoftwareUnitType type;

	/**
	 * @return the id
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @param id the id to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
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
	public SoftwareUnitType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(SoftwareUnitType type) {
		this.type = type;
	}

}