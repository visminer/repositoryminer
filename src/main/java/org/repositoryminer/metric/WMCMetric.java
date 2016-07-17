package org.repositoryminer.metric;

import java.util.List;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.listener.IMetricCalculationListener;

public class WMCMetric extends MethodBasedMetricTemplate{

	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, 
			IMetricCalculationListener listener) {
		int wmc = calculate(methods);
		listener.updateMetricValue(WMC, wmc);
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