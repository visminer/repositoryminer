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

@MetricAnnotation(name = "Number of Accessed Variables", 
description = "The total number of variables accessed directly from the measured operation. ", acronym = "NOAV")
public class NOAVMetric extends MethodBasedMetricTemplate {
	
	private List<Document> methodsDoc;

	@Override
	public void calculate(TypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		
		methodsDoc = new ArrayList<Document>();
		
		for(MethodDeclaration method : methods){
			int noav = calculate(method);
			methodsDoc.add(new Document("method", method.getName()).append("value", new Integer(noav)));
		}
		
		document.append("NOAV", new Document("methods", methodsDoc));
		
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
