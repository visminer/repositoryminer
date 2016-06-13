package org.repositoryminer.ast;

public class AbstractTypeDeclaration {

	public enum Archetype {
		CLASS_OR_INTERFACE, ENUM, ANNOTATION;
	}
	
	private Archetype archetype;
	private String name;

	/**
	 * @return the archetype
	 */
	public Archetype getArchetype() {
		return archetype;
	}

	/**
	 * @param archetype the archetype to set
	 */
	public void setArchetype(Archetype archetype) {
		this.archetype = archetype;
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