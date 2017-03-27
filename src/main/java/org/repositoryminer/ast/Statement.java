package org.repositoryminer.ast;

public class Statement {

	public enum NodeType {
		NONE,
		RETURN,
		IF,
		ELSE,
		SWITCH,
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
		VARIABLE_DECLARATION,
	}
	
	private NodeType nodeType;
	private String expression;
	private int nesting;
	
	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public int getNesting() {
		return nesting;
	}

	public void setNesting(int nesting) {
		this.nesting = nesting;
	}

}