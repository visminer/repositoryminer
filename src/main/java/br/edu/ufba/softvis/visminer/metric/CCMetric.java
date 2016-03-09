package br.edu.ufba.softvis.visminer.metric;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.ast.Statement;
import br.edu.ufba.softvis.visminer.ast.TypeDeclaration;
import br.edu.ufba.softvis.visminer.constant.NodeType;

@MetricAnnotation(name = "Ciclomatic Complexity", description = "Cyclomatic complexity is a software metric, used to indicate the complexity of a"
		+ " program. It is a quantitative measure of the number of linearly independent paths through a"
		+ " program's source code.", acronym = "CC")
public class CCMetric extends MethodBasedMetricTemplate {

	private List<Document> methodsDoc;

	@Override
	public void calculate(TypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		methodsDoc = new ArrayList<Document>();

		int ccClass = calculate(methods);
		document.append("CC", new Document("accumulated", new Integer(ccClass)).append("methods", methodsDoc));
	}

	// for classes
	public int calculate(List<MethodDeclaration> methods) {
		int ccClass = 0;
		for (MethodDeclaration method : methods) {

			int cc = calculate(method);
			ccClass += cc;
			methodsDoc.add(new Document("method", method.getName()).append("value", new Integer(cc)));
		}
		return ccClass;
	}

	// for methods
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
