package br.edu.ufba.softvis.visminer.ast;

import br.edu.ufba.softvis.visminer.constant.NodeType;

public class Statement {

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
