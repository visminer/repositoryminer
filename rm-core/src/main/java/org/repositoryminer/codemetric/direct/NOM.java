package org.repositoryminer.codemetric.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractType;

@DirectMetricProperties(id = MetricId.NOM)
public class NOM implements IDirectCodeMetric {

	private static final MetricId ID = MetricId.NOM;
	
	@Override
	public void calculate(AST ast) {
		ast.getMetrics().put(ID, ast.getMethods().size());
		for (AbstractType type : ast.getTypes()) {
			type.getMetrics().put(ID, type.getMethods().size());
		}
	}

}