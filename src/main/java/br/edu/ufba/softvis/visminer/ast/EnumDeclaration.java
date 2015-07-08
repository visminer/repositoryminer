package br.edu.ufba.softvis.visminer.ast;

import java.util.List;

public class EnumDeclaration {

	private int id;
	private String name;
	private List<EnumConstantDeclaration> declarations;
	
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
	 * @return the declarations
	 */
	public List<EnumConstantDeclaration> getDeclarations() {
		return declarations;
	}
	/**
	 * @param declarations the declarations to set
	 */
	public void setDeclarations(List<EnumConstantDeclaration> declarations) {
		this.declarations = declarations;
	}

}
