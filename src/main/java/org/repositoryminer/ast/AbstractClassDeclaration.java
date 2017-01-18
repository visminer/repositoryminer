package org.repositoryminer.ast;

public class AbstractClassDeclaration {

	public enum Archetype {
		CLASS_OR_INTERFACE, ENUM, ANNOTATION;
	}
	
	private Archetype archetype;
	private String name;
	private ExtendsDeclaration extendsDeclaration;

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

	public ExtendsDeclaration getExtendsDeclaration() {
		return extendsDeclaration;
	}

	public void setExtendsDeclaration(ExtendsDeclaration extendsDeclaration) {
		this.extendsDeclaration = extendsDeclaration;
	}

}