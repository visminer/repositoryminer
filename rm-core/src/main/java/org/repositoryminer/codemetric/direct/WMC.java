package org.repositoryminer.codemetric.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

@DirectMetricProperties(id = MetricId.WMC, requisites = { MetricId.CYCLO })
public class WMC implements IDirectCodeMetric {

	private static final MetricId ID = MetricId.WMC;

	@Override
	public void calculate(AST ast) {
		for (AbstractType type : ast.getTypes()) {
			int wmc = 0;
			for (AbstractMethod method : type.getMethods()) {
				wmc += (Integer) method.getMetrics().get(MetricId.CYCLO);
			}
			type.getMetrics().put(ID, wmc);
		}
	}

}