package org.repositoryminer.codemetric.direct;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.ast.Statement.NodeType;
import org.repositoryminer.codemetric.CodeMetricId;

/**
 * <h1>Number of Local Variables</h1>
 * <p>
 * LVAR is defined as the number of local variables inside a method.
 */
public class LVAR implements IDirectCodeMetric {

	private List<Document> methodsDoc = new ArrayList<Document>();
	
	@Override
	public CodeMetricId getId() {
		return CodeMetricId.LVAR;
	}
	
	@Override
	public Document calculate(AbstractClassDeclaration type, AST ast) {
		methodsDoc.clear();
		int accumulated = 0;
		
		for(MethodDeclaration method : type.getMethods()){
			int lvar = calculate(method);
			accumulated += lvar;
			methodsDoc.add(new Document("method", method.getName()).append("value", lvar));
		}
		
		return new Document("metric", CodeMetricId.LVAR.toString()).append("value", accumulated).append("methods", methodsDoc);
	}
	
	public int calculate(MethodDeclaration method){
		List<String> lvar = new ArrayList<String>();
		for(Statement statement : method.getStatements()){
			if(statement.getNodeType() == NodeType.VARIABLE_DECLARATION && !lvar.contains(statement.getExpression())) {
				lvar.add(statement.getExpression());
			}
		}
		return lvar.size();
	}

}