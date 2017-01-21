package org.repositoryminer.codemetric.direct;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;

public class WOC implements IDirectCodeMetric {

	@Override
	public Document calculate(AbstractClassDeclaration type, AST ast) {
		return new Document("metric", CodeMetricId.WOC.toString()).append("value",
				calculate(type.getMethods(), type.getFields()));
	}

	public float calculate(List<MethodDeclaration> methods, List<FieldDeclaration> fields) {
		int publicMembers = 0;
		int functionalMembers = 0;

		for (FieldDeclaration field : fields) {
			if (field.getModifiers().contains("public")) {
				publicMembers++;
			}
		}

		for (MethodDeclaration method : methods) {
			if (method.getModifiers().contains("public")) {
				publicMembers++;
				String name = method.getName();
				
				if ((!name.startsWith("is") && !name.startsWith("get") && !name.startsWith("set"))
						|| isNotAcessor(name, fields)) {
					functionalMembers++;
				}
			}
		}

		if (publicMembers == 0) {
			return 0f;
		}

		return functionalMembers * 1.0f / publicMembers;
	}

	private boolean isNotAcessor(String signature, List<FieldDeclaration> fields) {
		String field;

		if (signature.startsWith("get") || signature.startsWith("set")) {
			field = signature.substring(3, signature.indexOf('('));
		} else {
			field = signature.substring(2, signature.indexOf('('));
		}

		if (field.length() == 0) {
			return true;
		}

		char c[] = field.toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		String field2 = new String(c);

		for (FieldDeclaration fd : fields) {
			if (fd.getName().equals(field) || fd.getName().equals(field2)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public CodeMetricId getId() {
		return CodeMetricId.WOC;
	}

}