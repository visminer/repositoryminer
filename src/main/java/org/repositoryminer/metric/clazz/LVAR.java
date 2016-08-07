package org.repositoryminer.metric.clazz;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.ast.Statement.NodeType;
import org.repositoryminer.metric.MetricId;

/**
 * <h1>Number of Local Variables</h1>
 * <p>
 * LVAR is defined as the number of local variables inside a method.
 */
public class LVAR extends MethodBasedMetricTemplate {

	private List<Document> methodsDoc;
	
	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		
		methodsDoc = new ArrayList<Document>();
		int accumulated = 0;
		
		for(MethodDeclaration method : methods){
			int lvar = calculate(method);
			accumulated += lvar;
			methodsDoc.add(new Document("method", method.getName()).append("value", new Integer(lvar)));
		}
		
		document.append("name", MetricId.LVAR).append("accumulated", new Integer(accumulated)).append("methods", methodsDoc);
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