package org.repositoryminer.codemetric.direct;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;

public class NOAM implements IDirectCodeMetric {

	@Override
	public Document calculate(AbstractClassDeclaration type, AST ast) {
		return new Document("metric", CodeMetricId.NOAM.toString()).append("value",
				calculate(type.getMethods(), type.getFields()));
	}

	public int calculate(List<MethodDeclaration> methods, List<FieldDeclaration> fields) {
		int accessorMehtods = 0;
		for (MethodDeclaration method : methods) {
			if (method.getModifiers().contains("public") && isAcessor(method.getName(), fields)) {
				accessorMehtods++;
			}
		}
		return accessorMehtods;
	}
	
	public boolean isAcessor(String signature, List<FieldDeclaration> fields) {
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

		for (FieldDeclaration fd : fields) {
			if (fd.getName().equals(field) || fd.getName().equals(field2)) {
				return true;
			}
		}

		return false;
	}
	
	@Override
	public CodeMetricId getId() {
		return CodeMetricId.NOAM;
	}

}