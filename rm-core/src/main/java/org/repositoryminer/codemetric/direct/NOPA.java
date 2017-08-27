package org.repositoryminer.codemetric.direct;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;

public class NOPA implements IDirectCodeMetric {

	@Override
	public Document calculate(AbstractClassDeclaration type, AST ast) {
		return new Document("metric", CodeMetricId.NOPA.toString()).append("value", calculate(type.getFields()));
	}

	public int calculate(List<FieldDeclaration> fields) {
		int publicMembers = 0;

		for (FieldDeclaration field : fields) {
			if (field.getModifiers().contains("public")) {
				publicMembers++;
			}
		}

		return publicMembers;
	}

	@Override
	public CodeMetricId getId() {
		return CodeMetricId.NOPA;
	}

}