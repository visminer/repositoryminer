package org.repositoryminer.metric;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.TypeDeclaration;

public class PARMetric extends MethodBasedMetricTemplate {

	private List<Document> methodsDoc;
	
	@Override
	public void calculate(TypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		methodsDoc = new ArrayList<Document>();
		
		for(MethodDeclaration method : methods){
			int par = calculate(method);
			methodsDoc.add(new Document("method", method.getName()).append("value", new Integer(par)));
		}
		
		document.append("PAR", new Document("methods", methodsDoc));

	}
	
	public int calculate(MethodDeclaration method){
		return method.getParameters()!=null && !method.getParameters().isEmpty() ? method.getParameters().size() : 0;
	}

}
