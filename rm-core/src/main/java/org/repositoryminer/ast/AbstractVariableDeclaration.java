package org.repositoryminer.ast;

/**
 * This class represents a local variable declaration.
 */
public class AbstractVariableDeclaration extends AbstractStatement {

	private String type;
	private String initializer;

	public AbstractVariableDeclaration() {
		super(NodeType.VARIABLE_DECLARATION);
	}

	public AbstractVariableDeclaration(String expression, String type, String initializer) {
		this();
		setExpression(expression);
		this.type = type;
		this.initializer = initializer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInitializer() {
		return initializer;
	}

	public void setInitializer(String initializer) {
		this.initializer = initializer;
	}

}