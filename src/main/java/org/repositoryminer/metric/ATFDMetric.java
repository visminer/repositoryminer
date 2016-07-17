package org.repositoryminer.metric;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.ast.Statement.NodeType;
import org.repositoryminer.listener.IMetricCalculationListener;

public class ATFDMetric extends MethodBasedMetricTemplate {

	private Map<String, Integer> valuesPerMethod;

	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, 
			IMetricCalculationListener listener) {
		valuesPerMethod = new HashMap<String, Integer>();

		int atfdClass = calculate(type, methods, true);
		listener.updateMethodBasedMetricValue(ATFD, atfdClass, valuesPerMethod);
	}

	public int calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, boolean calculateByMethod) {
		int atfdClass = 0;

		for (MethodDeclaration mDeclaration : methods) {
			int atfdMethod = countForeignAccessedFields(type, mDeclaration);

			atfdClass += atfdMethod;
			if (calculateByMethod) {
				valuesPerMethod.put(mDeclaration.getName(), new Integer(atfdMethod));
			}
		}

		return atfdClass;
	}

	private int countForeignAccessedFields(AbstractTypeDeclaration currType, MethodDeclaration method) {
		Set<String> accessedFields = new HashSet<String>();

		for (Statement stm : method.getStatements()) {
			
			if (stm.getNodeType().equals(NodeType.FIELD_ACCESS)) {
				String exp = stm.getExpression();
				String type = exp.substring(0, exp.lastIndexOf("."));
				if (!currType.getName().equals(type))
					accessedFields.add(exp.toLowerCase());
				
			} else if (stm.getNodeType().equals(NodeType.METHOD_INVOCATION)) {
				
				String exp = stm.getExpression();
				String type = exp.substring(0, exp.lastIndexOf("."));
				String methodInv = exp.substring(exp.lastIndexOf(".") + 1);
				if (!currType.getName().equals(type)) {
					if ((methodInv.startsWith("get") || methodInv.startsWith("set")) && methodInv.length() > 3) {
						accessedFields.add((type + "." + methodInv.substring(3)).toLowerCase());
					} else if (methodInv.startsWith("is") && methodInv.length() > 2)
						accessedFields.add((type + "." + methodInv.substring(2)).toLowerCase());
				}
				
			}

		}

		return accessedFields.size();
	}

}
