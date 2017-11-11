package org.repositoryminer.metrics.codemetric;

import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.ast.AbstractMethod;
import org.repositoryminer.metrics.ast.AbstractStatement;
import org.repositoryminer.metrics.ast.AbstractType;
import org.repositoryminer.metrics.ast.NodeType;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.MethodReport;
import org.repositoryminer.metrics.report.ProjectReport;

public class CYCLO extends CodeMetric {

	public CYCLO() {
		super.id = CodeMetricId.CYCLO;
	}
	
	@Override
	public void calculate(AST ast, FileReport fileReport, ProjectReport projectReport) {
		for (AbstractType type : ast.getTypes()) {
			ClassReport cr = fileReport.getClass(type.getName());
			for (AbstractMethod method : type.getMethods()) {
				MethodReport mr = cr.getMethodBySignature(method.getName());
				mr.getMetricsReport().setCodeMetric(CodeMetricId.CYCLO, calculate(method));
			}
		}
	}

	public int calculate(AbstractMethod method) {
		if (method.getStatements() == null) {
			return 1;
		}

		int cc = 1;
		for (AbstractStatement statement : method.getStatements()) {
			switch (statement.getNodeType()) {
			case SWITCH_CASE:
				cc++;
				break;
			case IF:
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

	@Override
	public void clean(ProjectReport projectReport) {}

}