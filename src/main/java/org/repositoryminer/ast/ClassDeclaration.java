package org.repositoryminer.ast;

import java.util.ArrayList;
import java.util.List;

public class ClassDeclaration extends AbstractClassDeclaration{

	private boolean isInterface;
	private SuperClassDeclaration superClass;
	
	private List<SuperClassDeclaration> interfaces;
	

	public ClassDeclaration() {
		setArchetype(ClassArchetype.CLASS_OR_INTERFACE);
		this.interfaces = new ArrayList<>();

	}
	
	public boolean isInterface() {
		return isInterface;
	}

	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

	public SuperClassDeclaration getSuperClass() {
		return superClass;
	}

	public void setSuperClass(SuperClassDeclaration superClass) {
		this.superClass = superClass;
	}
	
	public List<SuperClassDeclaration> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<SuperClassDeclaration> interfaces) {
		this.interfaces = interfaces;
	}
	
	public void addInterface(SuperClassDeclaration interfacE){
		this.interfaces.add(interfacE);
	}
	
}