package org.repositoryminer.metric.clazz;

/**
 * <h1>Lines of Code</h1>
 * <p>
 * LOC is defined as the number of lines of code of an operation, including blank lines and comments.
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.metric.MetricId;

public class LOC implements IClassMetric {

	private Pattern pattern;

	public LOC() {
		pattern = Pattern.compile("(\r\n)|(\r)|(\n)");
	}

	@Override
	public void calculate(AbstractTypeDeclaration type, AST ast, Document document) {
		int sloc = calculate(ast.getSourceCode());

		document.append("name", MetricId.LOC).append("accumulated", new Integer(sloc));
	}

	public int calculate(String source) {
		if (source == null || source.length() == 0)
			return 0;

		Matcher m = pattern.matcher(source);

		// starts from 1 because the matcher doesn't count the last line (only
		// line breaks)
		int i = 1;

		while (m.find())
			i++;

		return i;
	}

}