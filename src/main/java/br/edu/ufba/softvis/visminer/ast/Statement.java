package br.edu.ufba.softvis.visminer.ast;

import br.edu.ufba.softvis.visminer.constant.NodeType;

public class Statement {

	private NodeType nodeType;
	private String expression;

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
