package org.repositoryminer.codemetric.direct;

import java.util.ArrayList;
import java.util.List;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractStatement;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.ast.NodeType;

@DirectMetricProperties(id = MetricId.LVAR)
public class LVAR implements IDirectCodeMetric {

	private static final MetricId ID = MetricId.LVAR;

	@Override
	public void calculate(AST ast) {
		for (AbstractMethod method : ast.getMethods()) {
			method.getMetrics().put(ID, calculate(method));
		}

		for (AbstractType type : ast.getTypes()) {
			for (AbstractMethod method : type.getMethods()) {
				method.getMetrics().put(ID, calculate(method));
			}
		}
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