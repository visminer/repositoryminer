package org.repositoryminer.ast;

public class Statement {

	public enum NodeType {
		NONE,
		RETURN,
		IF,
		ELSE,
		SWITCH_CASE,
		SWITCH_DEFAULT,
		FOR,
		DO_WHILE,
		WHILE,
		BREAK,
		CONTINUE,
		TRY,
		CATCH,
		FINALLY,
		THROW,
		CONDITIONAL_EXPRESSION,
		METHOD_INVOCATION,
		FIELD_ACCESS,
		VARIABLE,
		VARIABLE_ACCESS;
	}
	
	private NodeType nodeType;
	private String expression;
	
	/**
	 * @return the nodeType
	 */
	public NodeType getNodeType() {
		return nodeType;
	}
	/**
	 * @param nodeType the nodeType to set
	 */
	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}
	/**
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}
	/**
	 * @param expression the expression to set
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	
}
