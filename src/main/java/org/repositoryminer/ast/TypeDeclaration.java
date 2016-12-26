package org.repositoryminer.ast;

import java.util.List;

public class TypeDeclaration extends AbstractTypeDeclaration{

	private boolean isInterface;
	private List<MethodDeclaration> methods;
	private List<FieldDeclaration> fields;
	
	public TypeDeclaration() {
		setArchetype(Archetype.CLASS_OR_INTERFACE);
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
	
}