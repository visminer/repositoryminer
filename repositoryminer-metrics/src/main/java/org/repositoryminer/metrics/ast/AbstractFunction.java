package org.repositoryminer.metrics.ast;

import java.util.List;
import org.eclipse.cdt.core.dom.ast.IASTNode;

public class AbstractFunction extends AbstractType {
	
	private List<AbstractFunction> function;
	private IASTNode node;
	private String expression;
	private String bind;

	public AbstractFunction(IASTNode node) {
		this.node = node;
	}

	public AbstractFunction(IASTNode node, String expression, String bind) {
		this.node = node;
		this.expression = expression;
		this.bind = bind;
	}
	
	public String getBind() {
		return bind;
	}

	public void setBind(String bind) {
		this.bind = bind;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public List<AbstractFunction> getFunctions() {
		return function;
	}

	public void setFunctions(List<AbstractFunction> function) {
		this.function = function;
	}
	

}