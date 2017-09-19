package org.repositoryminer.codemetric.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

@DirectMetricProperties(id = MetricId.PAR)
public class PAR implements IDirectCodeMetric {

	private static final MetricId ID = MetricId.PAR;

	@Override
	public void calculate(AST ast) {
		for (AbstractMethod method : ast.getMethods()) {
			method.getMetrics().put(ID, method.getParameters().size());
		}

		for (AbstractType type : ast.getTypes()) {
			for (AbstractMethod method : type.getMethods()) {
				method.getMetrics().put(ID, method.getParameters().size());
			}
		}
	}

}