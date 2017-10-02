package org.repositoryminer.metric;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

public class WMC implements IMetric {

	private static final MetricID[] REQUIRED_METRICS = { MetricID.CYCLO };

	@Override
	public void calculate(AST ast) {
		for (AbstractType type : ast.getTypes()) {
			int wmc = 0;
			for (AbstractMethod method : type.getMethods())
				wmc += (Integer) method.getMetrics().get(MetricID.CYCLO);

			type.getMetrics().put(MetricID.WMC, wmc);
		}
	}

	@Override
	public MetricID getId() {
		return MetricID.WMC;
	}

	@Override
	public MetricID[] getRequiredMetrics() {
		return REQUIRED_METRICS;
	}

}