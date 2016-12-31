package org.repositoryminer.ast;

import java.util.List;

public class Document {

	private String name;
	private List<AbstractClassDeclaration> types;
	private List<MethodDeclaration> methods;
	private List<ImportDeclaration> imports;
	private String packageDeclaration;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AbstractClassDeclaration> getTypes() {
		return types;
	}

	public void setTypes(List<AbstractClassDeclaration> types) {
		this.types = types;
	}

	public List<MethodDeclaration> getMethods() {
		return methods;
	}

	public void setMethods(List<MethodDeclaration> methods) {
		this.methods = methods;
	}

	public List<ImportDeclaration> getImports() {
		return imports;
	}

	public void setImports(List<ImportDeclaration> imports) {
		this.imports = imports;
	}
	
	public String getPackageDeclaration() {
		return packageDeclaration;
	}

	public void setPackageDeclaration(String packageDeclaration) {
		this.packageDeclaration = packageDeclaration;
	}

}