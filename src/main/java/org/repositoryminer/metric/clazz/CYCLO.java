package org.repositoryminer.metric.clazz;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.ast.Statement.NodeType;
import org.repositoryminer.metric.MetricId;

/**
 * <h1>McCabe’s Cyclomatic Number</h1>
 * <p>
 * CYCLO is defined as "a quantitative measure of the number of linearly
 * independent paths through a program's source code".
 * <p>
 * CYCLO plays a key-role in understanding the overall complexity of code by
 * measuring the complexity of methods. It is calculated as follows:
 * <ul>
 * <li>if a decision statement is found (for-loop, while-loop, if), 1 must be
 * added to the value of the metric
 * <li>An extra 1 must be added to the final value since the main top path of
 * the method must also be taken into account. This meaning, ZERO is
 * <b>NEVER</b> the CC value of a method that has a body of statements.
 * </ul>
 * <p>
 * Our implementation of CC calculation is responsive to the following set of
 * decision statements:
 * <ul>
 * <li>IF -> simple conditional statement (enclosing only one conditional test)
 * <li>SWITCH CASE -> no matter how many case tests, it adds only 1 to the value
 * <li>FOR and WHILE
 * <li>CATCH -> we understand that the TRY.. block is not an actual path, but
 * the CATCH is since it behaves like an IF
 * <li>CONDITIONAL EXPRESSION -> the ones which are made of more than a single
 * conditional test. The expression is broken down to sub-conditional tests.
 * Each of the resulting tests adds 1 to the value of the metric.
 * </ul>
 */
public class CYCLO extends MethodBasedMetricTemplate {

	private List<Document> methodsDoc;

	/**
	 * @see MethodBasedMetricTemplate#calculate(AbstractTypeDeclaration, List,
	 *      AST, Document)
	 */
	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		methodsDoc = new ArrayList<Document>();
		int ccClass = calculate(methods);
		document.append("name", MetricId.CYCLO).append("accumulated", new Integer(ccClass)).append("methods", methodsDoc);
	}

	/**
	 * Iterates through all methods to calculate their complexity
	 * <p>
	 * Although CC is method-oriented, an accumulated value of the metric is
	 * provided
	 * 
	 * @param methods
	 *            list of methods from which the CC metric must be extracted
	 * @return an integer representing the total amount of CC in the class
	 */
	public int calculate(List<MethodDeclaration> methods) {
		int ccClass = 0;
		for (MethodDeclaration method : methods) {

			int cc = calculate(method);
			ccClass += cc;
			methodsDoc.add(new Document("method", method.getName()).append("value", new Integer(cc)));
		}
		return ccClass;
	}

	/**
	 * The actual calculation of CC
	 * <p>
	 * Conditional statements are analyzed to calculate the complexity of the
	 * method. Important to note that, even if no statement is found it must
	 * return 1.
	 * 
	 * @param method
	 *            instance of {@link org.repositoryminer.ast.MethodDeclaration}
	 *            which the CC metric must be extracted from
	 * @return the CC value of the method
	 */
	public int calculate(MethodDeclaration method) {
		if (method.getStatements() == null) {
			return 1;
		}

		int cc = 1;
		for (Statement statement : method.getStatements()) {
			switch (statement.getNodeType()) {
			case IF:
			case SWITCH_CASE:
			case FOR:
			case DO_WHILE:
			case WHILE:
			case CATCH:
			case CONDITIONAL_EXPRESSION:
				cc += calculateExpression(statement.getExpression(), statement.getNodeType());
				break;
			default:
				break;
			}
		}

		return cc;
	}

	/**
	 * Breaks down a complex conditional expression
	 * <p>
	 * For instance, 'if (x == 1) && (y == 2) {}' is divided into two
	 * conditional tests: (x == 1), (y == 2). In this case, the expression will
	 * end up adding 2 to value of CC metric
	 * 
	 * @param expression
	 *            the complex conditional expression to split
	 * @param type
	 *            the conditional statement that the complex expression belongs
	 *            to
	 * @return the value of CC metric obtained from the expression
	 */
	private int calculateExpression(String expression, NodeType type) {
		int cc = 1;
		char[] chars = expression.toCharArray();

		if (type != NodeType.CATCH) {
			for (int i = 0; i < chars.length - 1; i++) {
				char next = chars[i];
				if ((next == '&' || next == '|') && (next == chars[i + 1])) {
					cc++;
				}
			}
		} else {
			for (int i = 0; i < chars.length - 1; i++) {
				if (chars[i] == '|') {
					cc++;
				}
			}
		}

		return cc;
	}

}