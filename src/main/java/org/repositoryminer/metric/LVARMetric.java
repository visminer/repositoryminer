package org.repositoryminer.metric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.ast.Statement.NodeType;
import org.repositoryminer.listener.IMetricCalculationListener;

public class LVARMetric extends MethodBasedMetricTemplate {

	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, 
			IMetricCalculationListener listener) {
		
		Map<String, Integer> valuesPerMethod = new HashMap<String, Integer>();
		int accumulated = 0;
		
		for(MethodDeclaration method : methods){
			int lvar = calculate(method);
			accumulated += lvar;
			valuesPerMethod.put(method.getName(), new Integer(lvar));
		}
		
		listener.updateMethodBasedMetricValue(LVAR, accumulated, valuesPerMethod);
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