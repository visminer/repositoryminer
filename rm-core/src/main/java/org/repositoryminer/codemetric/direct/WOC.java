package org.repositoryminer.codemetric.direct;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;

public class WOC implements IDirectCodeMetric {

	private NOAM noam = new NOAM();

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

				if (!noam.isAcessor(method.getName(), fields)) {
					functionalMembers++;
				}
			}
		}

		if (publicMembers == 0) {
			return 0f;
		}

		return functionalMembers * 1.0f / publicMembers;
	}

	@Override
	public CodeMetricId getId() {
		return CodeMetricId.WOC;
	}

}