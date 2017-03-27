package org.repositoryminer.ast;

import java.util.List;

public class AbstractClassDeclaration {

	private ClassArchetype archetype;
	private List<MethodDeclaration> methods;
	private List<FieldDeclaration> fields;
	private String name;

	public ClassArchetype getArchetype() {
		return archetype;
	}

	public void setArchetype(ClassArchetype archetype) {
		this.archetype = archetype;
	}

	public List<MethodDeclaration> getMethods() {
		return methods;
	}

	public void setMethods(List<MethodDeclaration> methods) {
		this.methods = methods;
	}

	public List<FieldDeclaration> getFields() {
		return fields;
	}

	public void setFields(List<FieldDeclaration> fields) {
		this.fields = fields;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}