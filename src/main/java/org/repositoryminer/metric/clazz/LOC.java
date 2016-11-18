package org.repositoryminer.metric.clazz;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.metric.MetricId;
import org.repositoryminer.utility.StringUtils;

public class LOC implements IClassMetric {

	@Override
	public String getId() {
		return MetricId.LOC;
	}
	
	@Override
	public void calculate(AbstractTypeDeclaration type, AST ast, Document document) {
		int sloc = calculate(ast.getSourceCode());

		document.append("name", MetricId.LOC).append("value", new Integer(sloc));
	}

	public int calculate(String source) {
		if (source == null || source.length() == 0)
			return 0;

		return StringUtils.countNonEmptyLines(source);
	}

}