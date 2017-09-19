package org.repositoryminer.codesmell.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.codemetric.direct.MetricId;

@DirectCodeSmellProperties(id = CodeSmellId.LONG_METHOD, metrics = {MetricId.LOC})
public class LongMethod implements IDirectCodeSmell {

	private int mlocThreshold = 65;
	
	@Override
	public void detect(AST ast) {
		for (AbstractMethod method : ast.getMethods()) {
			int loc = (Integer) method.getMetrics().get(MetricId.LOC);
			if (loc > mlocThreshold) {
				method.getCodeSmells().add(CodeSmellId.LONG_METHOD);
			}
		}
		
		for (AbstractType type : ast.getTypes()) {
			for (AbstractMethod method : type.getMethods()) {
				int loc = (Integer) method.getMetrics().get(MetricId.LOC);
				if (loc > mlocThreshold) {
					method.getCodeSmells().add(CodeSmellId.LONG_METHOD);
				}
			}
		}
	}

}
