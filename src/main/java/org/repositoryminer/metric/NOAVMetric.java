package org.repositoryminer.metric;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.ast.Statement.NodeType;

public class NOAVMetric extends MethodBasedMetricTemplate {
	
	private List<Document> methodsDoc;

	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		methodsDoc = new ArrayList<Document>();
		
		for(MethodDeclaration method : methods){
			int noav = calculate(method);
			methodsDoc.add(new Document("method", method.getName()).append("value", new Integer(noav)));
		}
		
		document.append("name", NOAV).append("methods", methodsDoc);
	}
	
	public int calculate(MethodDeclaration method){
		int noav = 0;
		LVARMetric lvarMetric = new LVARMetric();
		PARMetric parMetric = new PARMetric();
		
		for(Statement stmt : method.getStatements()){
			if(NodeType.VARIABLE_ACCESS.equals(stmt.getNodeType()))
				noav++;
		}
		
		//removing variable declarations from count
		noav = noav - lvarMetric.calculate(method);
		//removing method parameters from count
		noav = noav - parMetric.calculate(method);
		
		return noav;
	}

}
