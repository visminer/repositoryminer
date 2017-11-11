package org.repositoryminer.metrics.codemetric;

import java.util.List;

import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.ast.AbstractField;
import org.repositoryminer.metrics.ast.AbstractMethod;
import org.repositoryminer.metrics.ast.AbstractType;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.ProjectReport;

public class NProtM extends CodeMetric {

	public NProtM() {
		super.id = CodeMetricId.NProtM;
	}
	
	@Override
	public void calculate(AST ast, FileReport fileReport, ProjectReport projectReport) {
		for (AbstractType type : ast.getTypes()) {
			ClassReport cr = fileReport.getClass(type.getName());
			cr.getMetricsReport().setCodeMetric(CodeMetricId.NProtM, calculate(type.getMethods(), type.getFields()));
		}
	}

	public int calculate(List<AbstractMethod> methods, List<AbstractField> fields) {
		int members = 0;

		for (AbstractMethod method : methods) {
			if (isProtected(method.getModifiers())) {
				members++;
			}
		}

		for (AbstractField field : fields) {
			if (isProtected(field.getModifiers())) {
				members++;
			}
		}

		return members;
	}

	public boolean isProtected(List<String> modifiers) {
		if (modifiers.contains("protected") || (!modifiers.contains("public") && !modifiers.contains("private"))) {
			return true;
		}
		return false;
	}

	@Override
	public void clean(ProjectReport projectReport) {}

}