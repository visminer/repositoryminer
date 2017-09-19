package org.repositoryminer.codemetric.direct;

import java.util.List;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractField;
import org.repositoryminer.ast.AbstractType;

@DirectMetricProperties(id = MetricId.NOPA)
public class NOPA implements IDirectCodeMetric {

	private static final MetricId ID = MetricId.NOPA;
	
	@Override
	public void calculate(AST ast) {
		for (AbstractType type : ast.getTypes()) {
			type.getMetrics().put(ID, calculate(type.getFields()));
		}
	}

	public int calculate(List<AbstractField> fields) {
		int publicMembers = 0;
		for (AbstractField field : fields) {
			if (field.getModifiers().contains("public")) {
				publicMembers++;
			}
		}
		return publicMembers;
	}

}