package org.repositoryminer.metrics.codemetric;

import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.ast.AbstractMethod;
import org.repositoryminer.metrics.ast.AbstractType;
import org.repositoryminer.metrics.report.ClassReport;
import org.repositoryminer.metrics.report.FileReport;
import org.repositoryminer.metrics.report.MethodReport;
import org.repositoryminer.metrics.report.ProjectReport;

public class AMLOC extends CodeMetric {
	
	private static final CodeMetricId[] REQUIRED_METRICS = { CodeMetricId.LOC, CodeMetricId.NOM };

	
	public AMLOC() {
		super.id = CodeMetricId.AMLOC;
		super.requiredMetrics = REQUIRED_METRICS;
	}

	@Override
	public void calculate(AST ast, FileReport fileReport, ProjectReport projectReport) {
		
		for(AbstractType type : ast.getTypes()  ) {
			ClassReport claReport = fileReport.getClass(type.getName());
			
			System.out.println(type.getName());

			double total = 0.0;
						
			for(AbstractMethod method: type.getMethods()) {
				
				MethodReport metReport = claReport.getMethodBySignature(method.getName());
				total += metReport.getMetricsReport().getCodeMetric(CodeMetricId.LOC,Integer.class);
				
				
			}
			
			double numMethods = (double) claReport.getMetricsReport().getCodeMetric(CodeMetricId.NOM,Integer.class);
			
			Double amlocValue = 0.0;
			if(numMethods > 0) {
				amlocValue =  (total/numMethods);
			}
			
			claReport.getMetricsReport().setCodeMetric(CodeMetricId.AMLOC, amlocValue );
			
			
		}
		
		
	}

	@Override
	public void clean(ProjectReport projectReport) {
		// TODO Auto-generated method stub
		
	}
	
	

}
