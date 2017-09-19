package org.repositoryminer.codemetric.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

@DirectMetricProperties(id = MetricId.NOAV, requisites = { MetricId.LVAR })
public class NOAV implements IDirectCodeMetric {

	private static final MetricId ID = MetricId.NOAV;

	@Override
	public void calculate(AST ast) {
		for (AbstractMethod method : ast.getMethods()) {
			method.getMetrics().put(ID, calculate(method));
		}

		for (AbstractType type : ast.getTypes()) {
			for (AbstractMethod method : type.getMethods()) {
				method.getMetrics().put(ID, calculate(method));
			}
		}
	}

	public int calculate(AbstractMethod method) {
		int accessFields = LAA.countAccessedFields(method);
		int nVar = (Integer) method.getMetrics().get(MetricId.LVAR);
		int nParams = method.getParameters().size();
		return accessFields + nVar + nParams;
	}

}