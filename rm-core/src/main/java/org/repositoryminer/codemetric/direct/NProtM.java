package org.repositoryminer.codemetric.direct;

import java.util.List;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractField;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

@DirectMetricProperties(id = MetricId.NProtM)
public class NProtM implements IDirectCodeMetric {

	private static final MetricId ID = MetricId.NProtM;
	
	@Override
	public void calculate(AST ast) {
		for (AbstractType type : ast.getTypes()) {
			type.getMetrics().put(ID, calculate(type.getMethods(), type.getFields()));
		}
	}

	public int calculate(List<AbstractMethod> methods, List<AbstractField> fields) {
		int members = 0;

		for (AbstractMethod method : methods) {
			if (isProtected(method.getModifiers())) {
				members++;
			}
		}

		for (AbstractField field : fields) {
			if (isProtected(field.getModifiers())) {
				members++;
			}
		}

		return members;
	}

	public boolean isProtected(List<String> modifiers) {
		if (modifiers.contains("protected") || (!modifiers.contains("public") && !modifiers.contains("private"))) {
			return true;
		}
		return false;
	}

}