package org.repositoryminer.ast;

public class SuperClassDeclaration extends AbstractClassDeclaration {

	private boolean isInterface;
	private String packageDeclaration;
	
	public boolean isInterface() {
		return isInterface;
	}
	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}
	public String getPackageDeclaration() {
		return packageDeclaration;
	}
	public void setPackageDeclaration(String packageDeclaration) {
		this.packageDeclaration = packageDeclaration;
	}
	
}