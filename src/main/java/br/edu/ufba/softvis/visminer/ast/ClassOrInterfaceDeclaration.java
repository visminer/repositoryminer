package br.edu.ufba.softvis.visminer.ast;

import java.util.List;

import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;

public class ClassOrInterfaceDeclaration extends TypeDeclaration{

	private boolean isInterface;
	private List<MethodDeclaration> methods;
	private List<FieldDeclaration> fields;
	
	
	public ClassOrInterfaceDeclaration(){
		this.setType(SoftwareUnitType.CLASS_OR_INTERFACE);
	}

	/**
	 * @return the isInterface
	 */
	public boolean isInterface() {
		return isInterface;
	}

	/**
	 * @param isInterface the isInterface to set
	 */
	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
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
	 * @return the fields
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
