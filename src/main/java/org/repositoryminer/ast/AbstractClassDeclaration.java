package org.repositoryminer.ast;

public class AbstractClassDeclaration {

	private ClassArchetype archetype;
	private String name;

	public ClassArchetype getArchetype() {
		return archetype;
	}

	public void setArchetype(ClassArchetype classArchetype) {
		this.archetype = classArchetype;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}