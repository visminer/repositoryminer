package org.repositoryminer.codemetric.direct;

import java.util.List;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractField;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

public class NOAM implements IDirectCodeMetric {

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
		return "NOAM";
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