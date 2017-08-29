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
			if (method.getModifiers().contains("public") && isAcessor(method.getName(), fields)) {
				accessorMehtods++;
			}
		}
		return accessorMehtods;
	}

	public boolean isAcessor(String signature, List<AbstractField> fields) {
		String field;

		if (signature.startsWith("get") || signature.startsWith("set")) {
			field = signature.substring(3, signature.indexOf('('));
		} else if (signature.startsWith("is")) {
			field = signature.substring(2, signature.indexOf('('));
		} else {
			return false;
		}

		if (field.length() == 0) {
			return false;
		}

		char c[] = field.toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		String field2 = new String(c);

		for (AbstractField fd : fields) {
			if (fd.getName().equals(field) || fd.getName().equals(field2)) {
				return true;
			}
		}

		return false;
	}

}