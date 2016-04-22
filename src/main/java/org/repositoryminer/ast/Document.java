package org.repositoryminer.ast;

import java.util.List;

public class Document {

	private String name;
	private List<AbstractTypeDeclaration> types;
	private List<MethodDeclaration> methods;
	private List<ImportDeclaration> imports;
	private String packageDeclaration;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the types
	 */
	public List<AbstractTypeDeclaration> getTypes() {
		return types;
	}

	/**
	 * @param types
	 *            the types to set
	 */
	public void setTypes(List<AbstractTypeDeclaration> types) {
		this.types = types;
	}

	/**
	 * @return the methods
	 */
	public List<MethodDeclaration> getMethods() {
		return methods;
	}

	/**
	 * @param imports
	 *            the methods to set
	 */
	public void setMethods(List<MethodDeclaration> methods) {
		this.methods = methods;
	}

	/**
	 * @return the imports
	 */
	public List<ImportDeclaration> getImports() {
		return imports;
	}

	/**
	 * @param imports
	 *            the imports to set
	 */
	public void setImports(List<ImportDeclaration> imports) {
		this.imports = imports;
	}
	
	/**
	 * @return the packageDeclaration
	 */
	public String getPackageDeclaration() {
		return packageDeclaration;
	}

	/**
	 * @param packageDeclaration
	 *            the packageDeclaration to set
	 */
	public void setPackageDeclaration(String packageDeclaration) {
		this.packageDeclaration = packageDeclaration;
	}

}