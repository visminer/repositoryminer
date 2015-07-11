package br.edu.ufba.softvis.visminer.metric;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.constant.MetricType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.model.bean.Commit;
import br.edu.ufba.softvis.visminer.model.bean.File;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;

@MetricAnnotation(
	name = "Number of Packages",
	description = "Number of Packages is a software metric used to measure the size of a computer program"+
				" by counting the number of packages.",
	acronym = "NOP",
	type = MetricType.COMPLEX,
	uid = MetricUid.NOP
	)
public class NOPMetric implements IMetric{

	@Override
	public void calculate(Map<File, AST> filesMap, List<Commit> commits,
			MetricPersistance persistence) {

		if(filesMap.size() == 0){
			return;
		}
		
		Set<Integer> packages = new HashSet<Integer>();
		int project = 0;
		
		for(Entry<File, AST> entry : filesMap.entrySet()){
			
			AST ast = entry.getValue();
			
			if(ast == null){
				continue;
			}
			
			project = ast.getProject().getId();
			if(ast.getDocument().getPackageDeclaration() != null){
				int id = ast.getDocument().getPackageDeclaration().getId();
				packages.add(id);
			}
		}
		
		
		
		int val = packages.size() + 1;
		persistence.postMetricValue(project, String.valueOf(val));
	}

}
