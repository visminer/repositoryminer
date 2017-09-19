package org.repositoryminer.codemetric.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractType;

@DirectMetricProperties(id = MetricId.NOA)
public class NOA implements IDirectCodeMetric {

	private static final MetricId ID = MetricId.NOA;

	@Override
	public void calculate(AST ast) {
		for (AbstractType type : ast.getTypes()) {
			type.getMetrics().put(ID, type.getFields().size());
		}
	}

}