package org.repositoryminer.ast;

import java.util.ArrayList;
import java.util.List;

public class AbstractClassDeclaration {

	private ClassArchetype archetype;
	private List<MethodDeclaration> methods;
	private List<FieldDeclaration> fields;
	private String name;
	private String fullQualifiedName;
	
	
	private List<String> parameters;
	
	private boolean isGeneric;
	
	
	public AbstractClassDeclaration(){
		this.parameters = new ArrayList<>();
	}
	

	public String getFullQualifiedName() {
		return fullQualifiedName;
	}

	public void setFullQualifiedName(String fullQualifiedName) {
		this.fullQualifiedName = fullQualifiedName;
	}

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


	public List<String> getParameters() {
		return parameters;
	}

	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}

	public boolean isGeneric() {
		return isGeneric;
	}

	public void setGeneric(boolean isGeneric) {
		this.isGeneric = isGeneric;
	}
	
	public void addParameter(String parameter){
		this.parameters.add(parameter);
	}

}