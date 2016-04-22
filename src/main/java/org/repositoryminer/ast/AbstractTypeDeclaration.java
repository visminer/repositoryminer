package org.repositoryminer.ast;

public class AbstractTypeDeclaration {

	private DeclarationType type;
	private String name;

	/**
	 * @return the type
	 */
	public DeclarationType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(DeclarationType type) {
		this.type = type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}