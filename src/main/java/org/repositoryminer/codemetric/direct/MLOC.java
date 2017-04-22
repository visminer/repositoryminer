package org.repositoryminer.codemetric.direct;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;

/**
 * <h1>Method Lines of Code</h1>
 * <p>
 * MLOC is defined as the number of non-blank and non-comment lines inside
 * method bodies.
 */
public class MLOC implements IDirectCodeMetric {

	private List<Document> methodsDoc = new ArrayList<Document>();
	private LOC locMetric = new LOC();

	@Override
	public CodeMetricId getId() {
		return CodeMetricId.MLOC;
	}

	@Override
	public Document calculate(AbstractClassDeclaration type, AST ast) {
		methodsDoc.clear();
		int accumulated = 0;

		for (MethodDeclaration method : type.getMethods()) {
			int mloc = calculate(method, ast);
			accumulated += mloc;
			methodsDoc.add(new Document("method", method.getName()).append("value", mloc));
		}

		return new Document("metric", CodeMetricId.MLOC.toString()).append("value", accumulated).append("methods",
				methodsDoc);
	}

	public int calculate(MethodDeclaration method, AST ast) {
		String methodSourceCode = ast.getSourceCode().substring(method.getStartPositionInSourceCode(),
				method.getEndPositionInSourceCode());
		return locMetric.calculate(methodSourceCode);
	}

}