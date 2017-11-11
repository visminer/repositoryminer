package org.repositoryminer.metrics.ast;

import java.util.List;

/**
 * This class represents an enum constant.
 */
public class AbstractEnumConstant {

	private String name;
	private List<String> arguments;

	public AbstractEnumConstant() {
	}
	
	public AbstractEnumConstant(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}

}