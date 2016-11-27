package org.repositoryminer.metric.clazz;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.metric.MetricId;

public class LOC implements IClassMetric {

	private Pattern pattern;
	
	public LOC() {
		pattern = Pattern.compile("(\r\n)|(\n)|(\r)");
	}
	
	@Override
	public MetricId getId() {
		return MetricId.LOC;
	}
	
	@Override
	public Document calculate(AbstractTypeDeclaration type, AST ast) {
		int sloc = calculate(ast.getSourceCode());
		return new Document("name", MetricId.LOC.toString()).append("value", new Integer(sloc));
	}

	public int calculate(String source) {
		if (source == null || source.length() == 0)
			return 0;

		int lines = 1;
		Matcher matcher = pattern.matcher(source);
		
		while (matcher.find())
			lines++;
		
		return lines;
	}

}