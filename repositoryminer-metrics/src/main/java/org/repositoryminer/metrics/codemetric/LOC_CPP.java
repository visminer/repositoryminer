package org.repositoryminer.metrics.codemetric;

import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.ast.AbstractMethod;
import org.repositoryminer.metrics.ast.AbstractType;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.MethodReport;
import org.repositoryminer.metrics.report.ProjectReport;


public class LOC_CPP  extends CodeMetric{
	
	
	public LOC_CPP(){
		super.id = CodeMetricId.LOC_CPP;
	}

	@Override
	public void calculate(AST ast, FileReport fileReport, ProjectReport projectReport) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clean(ProjectReport projectReport) {
		// TODO Auto-generated method stub
		
	}
	

}
