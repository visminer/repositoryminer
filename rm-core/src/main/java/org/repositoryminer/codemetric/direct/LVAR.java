package org.repositoryminer.codemetric.direct;

import java.util.ArrayList;
import java.util.List;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractStatement;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.ast.NodeType;

public class LVAR implements IDirectCodeMetric {

	@Override
	public Object calculateFromFile(AST ast) {
		return null;
	}

	@Override
	public Object calculateFromClass(AST ast, AbstractType type) {
		return null;
	}

	@Override
	public Object calculateFromMethod(AST ast, AbstractType type, AbstractMethod method) {
		return calculate(method);
	}

	@Override
	public String getMetric() {
		return "LVAR";
	}

	public int calculate(AbstractMethod method) {
		List<String> lvar = new ArrayList<String>();
		for (AbstractStatement statement : method.getStatements()) {
			if (statement.getNodeType() == NodeType.VARIABLE_DECLARATION && !lvar.contains(statement.getExpression())) {
				lvar.add(statement.getExpression());
			}
		}
		return lvar.size();
	}

}