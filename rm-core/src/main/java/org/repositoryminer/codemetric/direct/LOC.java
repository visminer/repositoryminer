package org.repositoryminer.codemetric.direct;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

public class LOC implements IDirectCodeMetric {

	private Pattern pattern;

	public LOC() {
		pattern = Pattern.compile("(\r\n)|(\n)|(\r)");
	}

	@Override
	public Object calculateFromFile(AST ast) {
		return calculate(ast.getSource());
	}

	@Override
	public Object calculateFromClass(AST ast, AbstractType type) {
		String clazz = ast.getSource().substring(type.getStartPosition(), type.getEndPosition());
		String body = clazz.substring(clazz.indexOf('{'));
		return calculate(body);
	}

	@Override
	public Object calculateFromMethod(AST ast, AbstractType type, AbstractMethod method) {
		String m = ast.getSource().substring(method.getStartPosition(), method.getEndPosition());
		return m.contains("{") ? calculate(m.substring(m.indexOf('{'))) : 0;
	}

	@Override
	public String getMetric() {
		return "LOC";
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