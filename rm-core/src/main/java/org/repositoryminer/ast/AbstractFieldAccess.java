package org.repositoryminer.ast;

public class AbstractFieldAccess extends AbstractStatement {

	private String type;
	private String declaringClass;
	private boolean primitive;
	private boolean builtIn;

	public AbstractFieldAccess() {
		super(NodeType.FIELD_ACCESS);
	}

	public AbstractFieldAccess(String expression, String type, String declaringClass, boolean primitive,
			boolean builtIn) {
		this();
		setExpression(expression);
		this.type = type;
		this.declaringClass = declaringClass;
		this.primitive = primitive;
		this.builtIn = builtIn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDeclaringClass() {
		return declaringClass;
	}

	public void setDeclaringClass(String declaringClass) {
		this.declaringClass = declaringClass;
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