package org.repositoryminer.metrics.ast;

import java.util.List;
import org.eclipse.cdt.core.dom.ast.IASTNode;

public class AbstractDeclaration extends AbstractType {
	
	private List<AbstractDeclaration> declaration;
	private IASTNode node;
	private NodeType nodeType;
	private String expression;

	public AbstractDeclaration(IASTNode node) {
		this.node = node;
	}

	public AbstractDeclaration(IASTNode node, String expression) {
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
	
	public List<AbstractDeclaration> getDeclarations() {
		return declaration;
	}

	public void setDeclarations(List<AbstractDeclaration> declaration) {
		this.declaration = declaration;
	}
	

}
