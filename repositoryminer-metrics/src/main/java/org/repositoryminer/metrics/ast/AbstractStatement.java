package org.repositoryminer.metrics.ast;

/**
 * This class represents a generic statement.
 */
public class AbstractStatement {

	private NodeType nodeType;
	private String expression;

	public AbstractStatement(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public AbstractStatement(NodeType nodeType, String expression) {
		this.nodeType = nodeType;
		this.expression = expression;
	}

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

}