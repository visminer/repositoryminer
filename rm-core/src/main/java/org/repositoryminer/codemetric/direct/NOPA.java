package org.repositoryminer.codemetric.direct;

import java.util.List;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractField;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

public class NOPA implements IDirectCodeMetric {

	@Override
	public Object calculateFromFile(AST ast) {
		return null;
	}

	@Override
	public Object calculateFromClass(AST ast, AbstractType type) {
		return calculate(type.getFields());
	}

	@Override
	public Object calculateFromMethod(AST ast, AbstractType type, AbstractMethod method) {
		return null;
	}

	@Override
	public String getMetric() {
		return "NOPA";
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