package org.repositoryminer.codesmell.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.codemetric.direct.MetricId;

@DirectCodeSmellProperties(id = CodeSmellId.COMPLEX_METHOD, metrics = {MetricId.CYCLO})
public class ComplexMethod implements IDirectCodeSmell {

	private int cycloThreshold = 10;

	@Override
	public void detect(AST ast) {
		for (AbstractMethod method : ast.getMethods()) {
			int cyclo = (Integer) method.getMetrics().get(MetricId.CYCLO);
			if (cyclo > cycloThreshold) {
				method.getCodeSmells().add(CodeSmellId.COMPLEX_METHOD);
			}
		}
		
		for (AbstractType type : ast.getTypes()) {
			for (AbstractMethod method : type.getMethods()) {
				int cyclo = (Integer) method.getMetrics().get(MetricId.CYCLO);
				if (cyclo > cycloThreshold) {
					method.getCodeSmells().add(CodeSmellId.COMPLEX_METHOD);
				}
			}
		}
	}

}