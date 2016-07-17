package org.repositoryminer.metric;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.listener.IMetricCalculationListener;

public class PARMetric extends MethodBasedMetricTemplate {

	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, 
			IMetricCalculationListener listener) {
		Map<String, Integer> valuesPerMethod = new HashMap<String, Integer>();

		int accumulated = 0;
		for(MethodDeclaration method : methods){
			int par = calculate(method);
			accumulated += par;
			valuesPerMethod.put(method.getName(), new Integer(par));
		}
	
		listener.updateMethodBasedMetricValue(PAR, accumulated, valuesPerMethod);
	}
	
	public int calculate(MethodDeclaration method){
		return method.getParameters()!=null && !method.getParameters().isEmpty() ? method.getParameters().size() : 0;
	}

}