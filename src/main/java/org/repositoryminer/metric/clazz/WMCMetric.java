package org.repositoryminer.metric.clazz;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration;

public class WMCMetric extends MethodBasedMetricTemplate{

	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		int wmc = calculate(methods);
		document.append("name", WMC).append("accumulated", new Integer(wmc));
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