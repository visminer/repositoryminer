package br.edu.ufba.softvis.visminer.ast;

import java.util.List;

public class Document {

	private int id;
	private String name;
	private List<TypeDeclaration> types;
	private List<ImportDeclaration> imports;
	private PackageDeclaration packageDeclaration;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the types
	 */
	public List<TypeDeclaration> getTypes() {
		return types;
	}
	/**
	 * @param types the types to set
	 */
	public void setTypes(List<TypeDeclaration> types) {
		this.types = types;
	}
	/**
	 * @return the imports
	 */
	public List<ImportDeclaration> getImports() {
		return imports;
	}
	/**
	 * @param imports the imports to set
	 */
	public void setImports(List<ImportDeclaration> imports) {
		this.imports = imports;
	}
	/**
	 * @return the packageDeclaration
	 */
	public PackageDeclaration getPackageDeclaration() {
		return packageDeclaration;
	}
	/**
	 * @param packageDeclaration the packageDeclaration to set
	 */
	public void setPackageDeclaration(PackageDeclaration packageDeclaration) {
		this.packageDeclaration = packageDeclaration;
	}
	
}