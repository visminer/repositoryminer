package org.repositoryminer.metric;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.NodeType;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.ast.TypeDeclaration;

public class LVARMetric extends MethodBasedMetricTemplate {

	private List<Document> methodsDoc;
	
	@Override
	public void calculate(TypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		
		methodsDoc = new ArrayList<Document>();
		
		for(MethodDeclaration method : methods){
			int lvar = calculate(method);
			methodsDoc.add(new Document("method", method.getName()).append("value", new Integer(lvar)));
		}
		
		document.append("LVAR", new Document("methods", methodsDoc));
	}
	
	public int calculate(MethodDeclaration method){
		List<String> lvar = new ArrayList<String>();

		for(Statement statement : method.getStatements()){
			if(NodeType.VARIABLE.equals(statement.getNodeType()) && !lvar.contains(statement.getExpression()))
				lvar.add(statement.getExpression());
		}
		
		return lvar.size();
	}

}
