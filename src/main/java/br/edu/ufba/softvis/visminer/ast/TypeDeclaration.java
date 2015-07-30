package br.edu.ufba.softvis.visminer.ast;

import java.util.List;

public class TypeDeclaration {

	private int id;
	private String name;
	private boolean interfaceClass;
	private List<MethodDeclaration> methods;
	private List<FieldDeclaration> fields;

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
	 * @return the interfaceClass
	 */
	public boolean isInterfaceClass() {
		return interfaceClass;
	}
	/**
	 * @param interfaceClass the interfaceClass to set
	 */
	public void setInterfaceClass(boolean interfaceClass) {
		this.interfaceClass = interfaceClass;
	}
	/**
	 * @return the methods
	 */
	public List<MethodDeclaration> getMethods() {
		return methods;
	}
	/**
	 * @param methods the methods to set
	 */
	public void setMethods(List<MethodDeclaration> methods) {
		this.methods = methods;
	}
	
	/**
	 * @return the Fields
	 */
	public List<FieldDeclaration> getFields() {
		return fields;
	}
	
	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<FieldDeclaration> fields) {
		this.fields = fields;
	}

}