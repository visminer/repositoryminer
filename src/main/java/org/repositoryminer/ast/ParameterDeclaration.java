package org.repositoryminer.ast;

public class ParameterDeclaration {

	private String type;
	private String name;
	
	
	private boolean primitiveType;
	private boolean arrayType;
	private boolean parametrizedType;
	
	public boolean isParametrizedType() {
		return parametrizedType;
	}

	public void setParametrizedType(boolean parametrizedType) {
		this.parametrizedType = parametrizedType;
	}

	public boolean isPrimitiveType() {
		return primitiveType;
	}

	public void setPrimitiveType(boolean primitiveType) {
		this.primitiveType = primitiveType;
	}

	public boolean isArrayType() {
		return arrayType;
	}

	public void setArrayType(boolean arrayType) {
		this.arrayType = arrayType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}