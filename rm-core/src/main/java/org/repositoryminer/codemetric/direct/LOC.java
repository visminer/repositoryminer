package org.repositoryminer.codemetric.direct;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

@DirectMetricProperties(id = MetricId.LOC)
public class LOC implements IDirectCodeMetric {

	private Pattern pattern;
	private static final MetricId ID = MetricId.LOC;

	public LOC() {
		pattern = Pattern.compile("(\r\n)|(\n)|(\r)");
	}

	@Override
	public void calculate(AST ast) {
		ast.getMetrics().put(ID, calculate(ast.getSource()));
		
		for (AbstractMethod method : ast.getMethods()) {
			method.getMetrics().put(ID, calculate(ast, method));
		}
		
		for (AbstractType type : ast.getTypes()) {
			type.getMetrics().put(ID, calculate(ast, type));
			for (AbstractMethod method : type.getMethods()) {
				method.getMetrics().put(ID, calculate(ast, method));
			}
		}
	}

	public int calculate(AST ast, AbstractType type) {
		String clazz = ast.getSource().substring(type.getStartPosition(), type.getEndPosition());
		String body = clazz.substring(clazz.indexOf('{'));
		return calculate(body);
	}

	public int calculate(AST ast, AbstractMethod method) {
		String m = ast.getSource().substring(method.getStartPosition(), method.getEndPosition());
		return m.contains("{") ? calculate(m.substring(m.indexOf('{'))) : 0;
	}

	public int calculate(String source) {
		if (source == null || source.length() == 0) {
			return 0;
		}

		int lines = 1;
		Matcher matcher = pattern.matcher(source);
		while (matcher.find()) {
			lines++;
		}

		return lines;
	}

}