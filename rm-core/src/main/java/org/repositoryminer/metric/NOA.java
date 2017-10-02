package org.repositoryminer.metric;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractType;

public class NOA implements IMetric {

	@Override
	public void calculate(AST ast) {
		for (AbstractType type : ast.getTypes())
			type.getMetrics().put(MetricID.NOA, type.getFields().size());
	}

	@Override
	public MetricID getId() {
		return MetricID.NOA;
	}

	@Override
	public MetricID[] getRequiredMetrics() {
		return null;
	}

}