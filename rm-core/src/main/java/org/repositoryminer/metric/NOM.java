package org.repositoryminer.metric;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractType;

public class NOM implements IMetric {

	@Override
	public void calculate(AST ast) {
		ast.getMetrics().put(MetricID.NOM, ast.getMethods().size());
		for (AbstractType type : ast.getTypes())
			type.getMetrics().put(MetricID.NOM, type.getMethods().size());
	}

	@Override
	public MetricID getId() {
		return MetricID.NOM;
	}

	@Override
	public MetricID[] getRequiredMetrics() {
		return null;
	}

}