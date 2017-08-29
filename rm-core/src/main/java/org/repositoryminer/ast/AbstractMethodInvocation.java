package org.repositoryminer.ast;

/**
 * This class represents a method invocation.
 */
public class AbstractMethodInvocation extends AbstractStatement {

	private String declaringExpression;

	public AbstractMethodInvocation() {
		super(NodeType.METHOD_INVOCATION);
	}

	public AbstractMethodInvocation(String expression, String declaringExpression) {
		this();
		setExpression(expression);
		this.declaringExpression = declaringExpression;
	}
	
	public String getDeclaringExpression() {
		return declaringExpression;
	}

	public void setDeclaringExpression(String declaringExpression) {
		this.declaringExpression = declaringExpression;
	}

}