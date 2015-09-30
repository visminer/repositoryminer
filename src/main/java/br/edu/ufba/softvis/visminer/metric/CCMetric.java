package br.edu.ufba.softvis.visminer.metric;

import java.util.List;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.ast.Statement;
import br.edu.ufba.softvis.visminer.constant.MetricType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.constant.NodeType;

@MetricAnnotation(
		name = "Ciclomatic Complexity",
		description = "Cyclomatic complexity is a software metric, used to indicate the complexity of a"
				+ " program. It is a quantitative measure of the number of linearly independent paths through a"
				+ " program's source code.",
				acronym = "CC",
				type = MetricType.COMMIT,
				uid = MetricUid.CC
		)
public class CCMetric extends MethodBasedMetricTemplate{

	@Override
	public void calculate(List<MethodDeclaration> methods) {
		for (MethodDeclaration method : methods) {
			
			int cc = calculate(method);
			persistence.postMetricValue(method.getId(), String.valueOf(cc));
		}
	}	
	
	@SuppressWarnings("incomplete-switch")
	private int calculate(MethodDeclaration method) {
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
			}
		}

		return cc;
	}
	
	private int calculateExpression(String expression, NodeType type) {
		int cc = 1;
		
		char[] chars = expression.toCharArray();
		
		if(type != NodeType.CATCH){
			for (int i = 0; i < chars.length - 1; i++) {
				char next = chars[i];
				if ((next == '&' || next == '|') && (next == chars[i + 1])) {
					cc++;
				}
			}
		}else{
			for (int i = 0; i < chars.length - 1; i++) {
				if (chars[i] == '|') {
					cc++;
				}
			}
		}
		
		return cc;
	}	
	
}
