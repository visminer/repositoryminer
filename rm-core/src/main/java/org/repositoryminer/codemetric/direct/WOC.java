package org.repositoryminer.codemetric.direct;

import java.util.List;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractField;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

public class WOC implements IDirectCodeMetric {

	@Override
	public Object calculateFromFile(AST ast) {
		return null;
	}

	@Override
	public Object calculateFromClass(AST ast, AbstractType type) {
		return calculate(type.getMethods(), type.getFields());
	}

	@Override
	public Object calculateFromMethod(AST ast, AbstractType type, AbstractMethod method) {
		return null;
	}

	@Override
	public String getMetric() {
		return "WOC";
	}

	public float calculate(List<AbstractMethod> methods, List<AbstractField> fields) {
		int publicMembers = 0;
		int functionalMembers = 0;

		for (AbstractField field : fields) {
			if (field.getModifiers().contains("public")) {
				publicMembers++;
			}
		}

		for (AbstractMethod method : methods) {
			if (method.getModifiers().contains("public")) {
				publicMembers++;
				if (!method.isAccessor()) {
					functionalMembers++;
				}
			}
		}

		return publicMembers == 0 ? 0 : functionalMembers * 1.0f / publicMembers;
	}

}
