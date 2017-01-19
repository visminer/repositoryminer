package org.repositoryminer.codemetric.direct;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.ClassArchetype;
import org.repositoryminer.ast.ClassDeclaration;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;

public class NProtM implements IDirectCodeMetric {

	@Override
	public Document calculate(AbstractClassDeclaration type, AST ast) {
		if (!type.getArchetype().equals(ClassArchetype.CLASS_OR_INTERFACE)) {
			return null;
		}

		ClassDeclaration cls = (ClassDeclaration) type;
		return new Document("metric", CodeMetricId.NProtM.toString()).append("value",
				calculate(cls.getMethods(), cls.getFields()));

	}

	public int calculate(List<MethodDeclaration> methods, List<FieldDeclaration> fields) {
		int members = 0;

		for (MethodDeclaration method : methods) {
			if (method.getModifiers().contains("protected")) {
				members++;
			}
		}

		for (FieldDeclaration field : fields) {
			if (field.getModifiers().contains("protected")) {
				members++;
			}
		}

		return members;
	}

	@Override
	public CodeMetricId getId() {
		return CodeMetricId.NProtM;
	}

}