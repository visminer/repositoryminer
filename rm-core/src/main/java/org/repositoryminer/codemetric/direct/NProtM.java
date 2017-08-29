package org.repositoryminer.codemetric.direct;

import java.util.List;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractField;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

public class NProtM implements IDirectCodeMetric {

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
		return "NPROTM";
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