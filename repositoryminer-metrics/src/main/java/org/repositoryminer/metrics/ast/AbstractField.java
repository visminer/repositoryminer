package org.repositoryminer.metrics.ast;

import java.util.List;

/**
 * This class represents a class field.
 */
public class AbstractField {

	private String name;
	private String type;
	private List<String> modifiers;
	private boolean primitive;
	private boolean builtIn;

	public AbstractField() {
	}

	public AbstractField(String name, String type, List<String> modifiers, boolean primitive, boolean builtIn) {
		this.name = name;
		this.type = type;
		this.modifiers = modifiers;
		this.primitive = primitive;
		this.builtIn = builtIn;
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

	public boolean isPrimitive() {
		return primitive;
	}

	public void setPrimitive(boolean primitive) {
		this.primitive = primitive;
	}

	public boolean isBuiltIn() {
		return builtIn;
	}

	public void setBuiltIn(boolean builtIn) {
		this.builtIn = builtIn;
	}

}