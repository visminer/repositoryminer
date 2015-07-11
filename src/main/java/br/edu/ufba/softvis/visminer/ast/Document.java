package br.edu.ufba.softvis.visminer.ast;

import java.util.List;

public class Document {

	private int id;
	private String name;
	private List<TypeDeclaration> typesDeclarations;
	private List<EnumDeclaration> enumsDeclarations;
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
	 * @return the typesDeclarations
	 */
	public List<TypeDeclaration> getTypesDeclarations() {
		return typesDeclarations;
	}
	/**
	 * @param typesDeclarations the typesDeclarations to set
	 */
	public void setTypesDeclarations(List<TypeDeclaration> typesDeclarations) {
		this.typesDeclarations = typesDeclarations;
	}
	/**
	 * @return the enumsDeclarations
	 */
	public List<EnumDeclaration> getEnumsDeclarations() {
		return enumsDeclarations;
	}
	/**
	 * @param enumsDeclarations the enumsDeclarations to set
	 */
	public void setEnumsDeclarations(List<EnumDeclaration> enumsDeclarations) {
		this.enumsDeclarations = enumsDeclarations;
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