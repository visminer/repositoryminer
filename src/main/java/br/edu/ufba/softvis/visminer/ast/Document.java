package br.edu.ufba.softvis.visminer.ast;

import java.util.List;

public class Document {

	private String uid;
	private String name;
	private List<TypeDeclaration> types;
	private List<MethodDeclaration> methods;
	private List<ImportDeclaration> imports;
	private PackageDeclaration packageDeclaration;
	
	/**
	 * @return the id
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @param id the id to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
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
	 * @return the methods
	 */
	public List<MethodDeclaration> getMethods() {
		return methods;
	}
	/**
	 * @param imports the methods to set
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Document other = (Document) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}