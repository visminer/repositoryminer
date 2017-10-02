package org.repositoryminer.metric;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

public class PAR implements IMetric {

	@Override
	public void calculate(AST ast) {
		for (AbstractMethod method : ast.getMethods())
			method.getMetrics().put(MetricID.PAR, method.getParameters().size());

		for (AbstractType type : ast.getTypes())
			for (AbstractMethod method : type.getMethods())
				method.getMetrics().put(MetricID.PAR, method.getParameters().size());
	}

	@Override
	public MetricID getId() {
		return MetricID.PAR;
	}

	@Override
	public MetricID[] getRequiredMetrics() {
		return null;
	}

}