package org.repositoryminer.ast;

public class AbstractTypeDeclaration {

	public enum Archetype {
		CLASS_OR_INTERFACE, ENUM, ANNOTATION;
	}
	
	private Archetype archetype;
	private String name;

	public Archetype getArchetype() {
		return archetype;
	}

	public void setArchetype(Archetype archetype) {
		this.archetype = archetype;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}