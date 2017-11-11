package org.repositoryminer.metrics.codemetric;

import java.util.List;

import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.ast.AbstractField;
import org.repositoryminer.metrics.ast.AbstractType;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.ProjectReport;

public class NOPA extends CodeMetric {

	public NOPA() {
		super.id = CodeMetricId.NOPA;
	}
	
	@Override
	public void calculate(AST ast, FileReport fileReport, ProjectReport projectReport) {
		for (AbstractType type : ast.getTypes()) {
			ClassReport cr = fileReport.getClass(type.getName());
			cr.getMetricsReport().setCodeMetric(CodeMetricId.NOPA, calculate(type.getFields()));
		}
	}

	public int calculate(List<AbstractField> fields) {
		int publicMembers = 0;
		for (AbstractField field : fields) {
			if (field.getModifiers().contains("public")) {
				publicMembers++;
			}
		}
		return publicMembers;
	}

	@Override
	public void clean(ProjectReport projectReport) {}

}