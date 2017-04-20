package org.repositoryminer.ast;

import java.util.ArrayList;
import java.util.List;

public class FieldDeclaration {

	private String name;
	private String type;
	private String arrayTypeName;
	private List<String> paramTypes = new ArrayList<>();

	public List<String> getParamTypes() {
		return paramTypes;
	}

	public void setParamTypes(List<String> paramTypes) {
		this.paramTypes = paramTypes;
	}

	private List<String> modifiers;
	
	private boolean primitiveType;
	private boolean arrayType;
	private boolean parametrizedType;
	private boolean isGeneric;

	public boolean isGeneric() {
		return isGeneric;
	}

	public void setGeneric(boolean isGeneric) {
		this.isGeneric = isGeneric;
	}

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
	
	public String getArrayTypeName() {
		return arrayTypeName;
	}

	public void setArrayTypeName(String arrayTypeName) {
		this.arrayTypeName = arrayTypeName;
	}

	public void addParametrizedType(String qualifiedName) {
		this.paramTypes.add(qualifiedName);
	}


	

}