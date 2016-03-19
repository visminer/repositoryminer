package br.edu.ufba.softvis.visminer.metric;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.ast.Statement;
import br.edu.ufba.softvis.visminer.ast.TypeDeclaration;
import br.edu.ufba.softvis.visminer.constant.NodeType;

@MetricAnnotation(name = "Number of local variables", description = "Number of local variables is a software metric used to indicate "
		+ "the total number of local variables in a function.", acronym = "LVAR")
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
