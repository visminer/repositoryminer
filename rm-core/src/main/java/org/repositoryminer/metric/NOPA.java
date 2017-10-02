package org.repositoryminer.metric;

import java.util.List;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractField;
import org.repositoryminer.ast.AbstractType;

public class NOPA implements IMetric {

	@Override
	public void calculate(AST ast) {
		for (AbstractType type : ast.getTypes())
			type.getMetrics().put(MetricID.NOPA, calculate(type.getFields()));
	}

	public int calculate(List<AbstractField> fields) {
		int publicMembers = 0;
		for (AbstractField field : fields)
			if (field.getModifiers().contains("public"))
				publicMembers++;

		return publicMembers;
	}

	@Override
	public MetricID getId() {
		return MetricID.NOPA;
	}

	@Override
	public MetricID[] getRequiredMetrics() {
		return null;
	}

}