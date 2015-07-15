package br.edu.ufba.softvis.visminer.metric;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.ast.TypeDeclaration;
import br.edu.ufba.softvis.visminer.constant.MetricType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.model.bean.Commit;
import br.edu.ufba.softvis.visminer.model.bean.File;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;

@MetricAnnotation(
		name = "Weighted Method Count",
		description = "Weighted Method Count is the sum of the statical complexity of all methods of a class." 
				+ " The Cyclomatic complexity metric is used to quantify the method’s complexity.",
				acronym = "WMC",
				type = MetricType.SIMPLE,
				uid = MetricUid.WMC
		)

public class WMCMetric implements IMetric{

	@Override
	public void calculate(Map<File, AST> filesMap, List<Commit> commits,
			MetricPersistance persistence) {
		// TODO Auto-generated method stub
		
		
		for(Entry<File, AST> entry : filesMap.entrySet()){

			AST ast = entry.getValue();
			
			if(ast == null || ast.getDocument().getTypesDeclarations() == null){
				continue;
			}

			for(TypeDeclaration type : ast.getDocument().getTypesDeclarations()){
				
				if(type.getMethods() == null){
					break;
				}
				
				int wmc = 0;				
				
				for(MethodDeclaration method : type.getMethods())			
					wmc += Integer.parseInt(persistence.getMetricValue(MetricUid.CC, method.getId(), commits.get(commits.size()-1)));	
					
				persistence.saveMetricValue(type.getId(), String.valueOf(wmc));				
			}

		}	
		
	}

}
