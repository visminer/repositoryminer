package org.repositoryminer.metrics.ast;

import java.util.List;
import org.eclipse.cdt.core.dom.ast.IASTNode;

public class AbstractAttribute extends AbstractType {
	
	private List<AbstractAttribute> attribute;
	private IASTNode node;
	private NodeType nodeType;
	private String expression;

	public AbstractAttribute(IASTNode node) {
		this.node = node;
	}

	public AbstractAttribute(IASTNode node, String expression) {
		this.node = node;
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
	
	public List<AbstractAttribute> getAttributes() {
		return attribute;
	}

	public void setAttributes(List<AbstractAttribute> attribute) {
		this.attribute = attribute;
	}
	

}
