package br.edu.ufba.softvis.visminer.metric;

import java.util.List;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.constant.MetricType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.model.business.Commit;

@MetricAnnotation(
		name = "Weighted Method Count",
		description = "Weighted Method Count is the sum of the statical complexity of all methods of a class." 
				+ " The Cyclomatic complexity metric is used to quantify the method’s complexity.",
				acronym = "WMC",
				type = MetricType.SNAPSHOT,
				uid = MetricUid.WMC,
				requisites = {MetricUid.CC}
		)

public class WMCMetric extends MethodBasedMetricTemplate{

	@Override
	public void calculate(List<MethodDeclaration> methods) {

		int wmc = 0;				

		for(MethodDeclaration method : methods){
			Commit c = commits.get(commits.size() - 1);
			wmc += Integer.parseInt(persistence.getMetricValue(MetricUid.CC, method.getId(), c));	
		}

		persistence.postMetricValue(currentType.getId(), String.valueOf(wmc));

	}

}