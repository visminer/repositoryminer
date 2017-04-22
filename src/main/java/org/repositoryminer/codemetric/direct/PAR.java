package org.repositoryminer.codemetric.direct;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;

/**
 * <h1>Number of Parameters</h1>
 * <p>
 * PAR is defined as the number of parameters per method.
 */
public class PAR implements IDirectCodeMetric {

	private List<Document> methodsDoc = new ArrayList<Document>();

	@Override
	public CodeMetricId getId() {
		return CodeMetricId.PAR;
	}

	@Override
	public Document calculate(AbstractClassDeclaration type, AST ast) {
		methodsDoc.clear();
		int accumulated = 0;

		for(MethodDeclaration method : type.getMethods()){
			int par = calculate(method);
			accumulated += par;
			methodsDoc.add(new Document("method", method.getName()).append("value", par));
		}

		return new Document("metric", CodeMetricId.PAR.toString()).append("value", accumulated).append("methods", methodsDoc);
	}

	public int calculate(MethodDeclaration method){
		return method.getParameters() != null ? method.getParameters().size() : 0;
	}

}