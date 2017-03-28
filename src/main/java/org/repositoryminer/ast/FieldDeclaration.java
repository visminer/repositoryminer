package org.repositoryminer.ast;

import java.util.List;

public class FieldDeclaration {

	private String name;
	private String type;
	private List<String> modifiers;
	
	private boolean primitiveType;
	private boolean arrayType;
	private boolean parametrizedType;


	public boolean isParametrizedType() {
		return parametrizedType;
	}

	public void setParametrizedType(boolean parametrizedType) {
		this.parametrizedType = parametrizedType;
	}

	public boolean isArrayType() {
		return arrayType;
	}

	public void setArrayType(boolean arrayType) {
		this.arrayType = arrayType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}

	public boolean isPrimitiveType() {
		return primitiveType;
	}

	public void setPrimitiveType(boolean primitiveType) {
		this.primitiveType = primitiveType;
	}
	
	
	

}