package org.repositoryminer.metric;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.ast.Statement.NodeType;
import org.repositoryminer.listener.IMetricCalculationListener;

public class NOAVMetric extends MethodBasedMetricTemplate {
	
	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, 
			IMetricCalculationListener listener) {
		Map<String, Integer> valuesPerMethod = new HashMap<String, Integer>();
		
		int accumulated = 0;
		for(MethodDeclaration method : methods){
			int noav = calculate(method);
			valuesPerMethod.put(method.getName(), new Integer(noav));
			accumulated += noav;
		}
		
		listener.updateMethodBasedMetricValue(NOAV, accumulated, valuesPerMethod);
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
