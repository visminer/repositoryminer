package org.repositoryminer.ast;

public class ImportDeclaration {

	private String name;
	private boolean isStatic;
	private boolean isOnDemand;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public boolean isOnDemand() {
		return isOnDemand;
	}

	public void setOnDemand(boolean isOnDemand) {
		this.isOnDemand = isOnDemand;
	}

}