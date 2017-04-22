package org.repositoryminer.codemetric.direct;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;

public class NProtM implements IDirectCodeMetric {

	@Override
	public Document calculate(AbstractClassDeclaration type, AST ast) {
		return new Document("metric", CodeMetricId.NProtM.toString()).append("value",
				calculate(type.getMethods(), type.getFields()));

	}

	public int calculate(List<MethodDeclaration> methods, List<FieldDeclaration> fields) {
		int members = 0;

		for (MethodDeclaration method : methods) {
			if (isProtected(method.getModifiers())) {
				members++;
			}
		}

		for (FieldDeclaration field : fields) {
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
	
	@Override
	public CodeMetricId getId() {
		return CodeMetricId.NProtM;
	}

}