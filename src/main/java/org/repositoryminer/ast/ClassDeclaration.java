package org.repositoryminer.ast;

public class ClassDeclaration extends AbstractClassDeclaration{

	private boolean isInterface;
	private SuperClassDeclaration superClass;
	
	public ClassDeclaration() {
		setArchetype(ClassArchetype.CLASS_OR_INTERFACE);
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
	
}