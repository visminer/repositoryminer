package org.repositoryminer.metric;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

public class NOAV implements IMetric {

	private static final MetricID[] REQUIRED_METRICS = { MetricID.LVAR };

	@Override
	public void calculate(AST ast) {
		for (AbstractMethod method : ast.getMethods())
			method.getMetrics().put(MetricID.NOAV, calculate(method));

		for (AbstractType type : ast.getTypes())
			for (AbstractMethod method : type.getMethods())
				method.getMetrics().put(MetricID.NOAV, calculate(method));
	}

	public int calculate(AbstractMethod method) {
		int accessFields = LAA.countAccessedFields(method);
		int nVar = (Integer) method.getMetrics().get(MetricID.LVAR);
		int nParams = method.getParameters().size();
		return accessFields + nVar + nParams;
	}

	@Override
	public MetricID getId() {
		return MetricID.NOAV;
	}

	@Override
	public MetricID[] getRequiredMetrics() {
		return REQUIRED_METRICS;
	}

}