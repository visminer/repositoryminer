package org.repositoryminer.metric;

import java.math.BigDecimal;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractType;

public class AMW implements IMetric {

	private static final MetricID[] REQUIRED_METRICS = {MetricID.WMC, MetricID.NOM};
	
	@Override
	public void calculate(AST ast) {
		for (AbstractType type : ast.getTypes()) {
			int wmc = (Integer) type.getMetrics().get(MetricID.WMC);
			int nom = (Integer) type.getMetrics().get(MetricID.NOM);
			type.getMetrics().put(MetricID.AMW, calculate(wmc, nom));
		}
	}

	public float calculate(int wmc, int nom) {
		if (nom == 0)
			return 0l;

		return new BigDecimal(wmc * 1f / nom).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	@Override
	public MetricID getId() {
		return MetricID.AMW;
	}

	@Override
	public MetricID[] getRequiredMetrics() {
		return REQUIRED_METRICS;
	}

}