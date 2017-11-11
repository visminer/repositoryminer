package org.repositoryminer.metrics.codemetric;

import java.util.List;

import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.ast.AbstractField;
import org.repositoryminer.metrics.ast.AbstractMethod;
import org.repositoryminer.metrics.ast.AbstractType;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.ProjectReport;

public class NOAM extends CodeMetric {

	public NOAM() {
		super.id = CodeMetricId.NOAM;
	}
	
	@Override
	public void calculate(AST ast, FileReport fileReport, ProjectReport projectReport) {
		for (AbstractType type : ast.getTypes()) {
			ClassReport cr = fileReport.getClass(type.getName());
			cr.getMetricsReport().setCodeMetric(CodeMetricId.NOAM, calculate(type.getMethods(), type.getFields()));
		}
	}

	public int calculate(List<AbstractMethod> methods, List<AbstractField> fields) {
		int accessorMehtods = 0;
		for (AbstractMethod method : methods) {
			if (method.getModifiers().contains("public") && method.isAccessor()) {
				accessorMehtods++;
			}
		}

		return accessorMehtods;
	}

	@Override
	public void clean(ProjectReport projectReport) {}

}