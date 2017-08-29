package org.repositoryminer.ast;

import java.util.List;

/**
 * This class represents a class field.
 */
public class AbstractField {

	private String name;
	private String type;
	private List<String> modifiers;

	public AbstractField() {
	}

	public AbstractField(String name, String type, List<String> modifiers) {
		this.name = name;
		this.type = type;
		this.modifiers = modifiers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}

}