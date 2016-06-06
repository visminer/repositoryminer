package org.repositoryminer.metric;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.MethodDeclaration;

public class MaxNestingMetric extends MethodBasedMetricTemplate {

	private List<Document> methodsDoc;

	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		methodsDoc = new ArrayList<Document>();
		for(MethodDeclaration method : methods){
			int maxNesting = calculate(method); 
			methodsDoc.add(new Document("method", method.getName()).append("value", new Integer(maxNesting)));
		}
		document.append("name", new String("MAXNESTING")).append("methods", methodsDoc);
	}

	public int calculate(MethodDeclaration method){
		return method.getMaxNesting(); //FIXME solve inconsistency of the values found
	}

}