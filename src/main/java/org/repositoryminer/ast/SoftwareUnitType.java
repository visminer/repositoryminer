package org.repositoryminer.ast;

public enum SoftwareUnitType {
	
	CLASS_OR_INTERFACE("CLASS_OR_INTERFACE"), ENUM("ENUM");

	private String id;

	private SoftwareUnitType(String id) {
		this.id = id;
	}

	/**
	 * @return Software unit type identifier.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @param id
	 * @return Software unit type with given id.
	 */
	public static SoftwareUnitType parse(String id) {

		for (SoftwareUnitType softwareUnitType : SoftwareUnitType.values()) {
			if (softwareUnitType.getId().equals(id)) {
				return softwareUnitType;
			}
		}

		return null;

	}
}
