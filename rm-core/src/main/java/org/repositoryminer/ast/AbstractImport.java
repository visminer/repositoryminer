package org.repositoryminer.ast;

/**
 * This class represents a import declaration.
 */
public class AbstractImport {

	private String name;
	private boolean isStatic;
	private boolean isOnDemand;

	public AbstractImport() {
	}

	public AbstractImport(String name, boolean isStatic, boolean isOnDemand) {
		this.name = name;
		this.isStatic = isStatic;
		this.isOnDemand = isOnDemand;
	}

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