package org.repositoryminer.codemetric.direct;

import java.util.List;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractField;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

@DirectMetricProperties(id = MetricId.NOAM)
public class NOAM implements IDirectCodeMetric {

	private static final MetricId ID = MetricId.NOAM;

	@Override
	public void calculate(AST ast) {
		for (AbstractType type : ast.getTypes()) {
			type.getMetrics().put(ID, calculate(type.getMethods(), type.getFields()));
		}
	}

	public int calculate(List<AbstractMethod> methods, List<AbstractField> fields) {
		int accessorMehtods = 0;
		for (AbstractMethod method : methods) {
			if (method.getModifiers().contains("public") && method.isAccessor()) {
				accessorMehtods++;
			}
		}
		return accessorMehtods;
	}

}