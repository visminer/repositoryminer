package org.repositoryminer.metrics.codemetric;

import java.math.BigDecimal;
import java.util.List;

import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.ast.AbstractField;
import org.repositoryminer.metrics.ast.AbstractMethod;
import org.repositoryminer.metrics.ast.AbstractType;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.ProjectReport;

public class WOC extends CodeMetric {

	public WOC() {
		super.id = CodeMetricId.WOC;
	}
	
	@Override
	public void calculate(AST ast, FileReport fileReport, ProjectReport projectReport) {
		for (AbstractType type : ast.getTypes()) {
			ClassReport cr = fileReport.getClass(type.getName());
			cr.getMetricsReport().setCodeMetric(CodeMetricId.WOC, calculate(type.getMethods(), type.getFields()));
		}
	}

	public double calculate(List<AbstractMethod> methods, List<AbstractField> fields) {
		int publicMembers = 0;
		int functionalMembers = 0;

		for (AbstractField field : fields) {
			if (field.getModifiers().contains("public")) {
				publicMembers++;
			}
		}

		for (AbstractMethod method : methods) {
			if (method.getModifiers().contains("public")) {
				publicMembers++;
				if (!method.isAccessor()) {
					functionalMembers++;
				}
			}
		}

		double result = publicMembers == 0 ? 0 : functionalMembers * 1.0 / publicMembers;
		return new BigDecimal(result).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	@Override
	public void clean(ProjectReport projectReport) {}

}