package org.repositoryminer.metric;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.listener.IMetricCalculationListener;

public class MaxNestingMetric extends MethodBasedMetricTemplate {

	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, 
			IMetricCalculationListener listener) {
		Map<String, Integer> valuesPerMethod = new HashMap<String, Integer>();
		for(MethodDeclaration method : methods){
			int maxNesting = calculate(method); 
			valuesPerMethod.put(method.getName(), new Integer(maxNesting));
		}
		
		listener.updateMethodBasedMetricValue(MAX_NESTING, 0, valuesPerMethod);
	}

	public int calculate(MethodDeclaration method){
		//FIXME solve inconsistency of the values found
		return method.getMaxNesting();
	}

}