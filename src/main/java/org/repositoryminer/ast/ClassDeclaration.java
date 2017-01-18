package org.repositoryminer.ast;

import java.util.List;

public class ClassDeclaration extends AbstractClassDeclaration{

	private boolean isInterface;
	private List<MethodDeclaration> methods;
	private List<FieldDeclaration> fields;
	private SuperClassDeclaration superClass;
	
	public ClassDeclaration() {
		setArchetype(ClassArchetype.CLASS_OR_INTERFACE);
	}
	
	public boolean isInterface() {
		return isInterface;
	}

	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
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

	public SuperClassDeclaration getSuperClass() {
		return superClass;
	}

	public void setSuperClass(SuperClassDeclaration superClass) {
		this.superClass = superClass;
	}
	
}