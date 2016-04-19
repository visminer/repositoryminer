package org.repositoryminer.metric;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.TypeDeclaration;

public class WMCMetric extends MethodBasedMetricTemplate{

	@Override
	public void calculate(TypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		int wmc = calculate(methods);

		document.append("WMC", new Document("accumulated", new Integer(wmc)));
	}
	
	public int calculate(List<MethodDeclaration> methods){
		int wmc = 0;
		CCMetric cc = new CCMetric(); 

		for(MethodDeclaration method : methods){
			wmc += cc.calculate(method);	
		}
		
		return wmc;
	}

}