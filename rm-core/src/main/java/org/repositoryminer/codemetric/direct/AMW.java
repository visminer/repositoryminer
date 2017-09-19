package org.repositoryminer.codemetric.direct;

import java.math.BigDecimal;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractType;

@DirectMetricProperties(id = MetricId.AMW, requisites = {MetricId.WMC, MetricId.NOM})
public class AMW implements IDirectCodeMetric {

	private static final MetricId ID = MetricId.AMW;

	@Override
	public void calculate(AST ast) {
		for (AbstractType type : ast.getTypes()) {
			int wmc = (Integer) type.getMetrics().get(MetricId.WMC);
			int nom = (Integer) type.getMetrics().get(MetricId.NOM);
			type.getMetrics().put(ID, calculate(wmc, nom));
		}
	}

	public float calculate(int wmc, int nom) {
		if (nom == 0) {
			return 0l;
		}
		return new BigDecimal(wmc * 1f / nom).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}

}
