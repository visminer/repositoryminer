package br.edu.ufba.softvis.visminer.metric;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.Document;
import br.edu.ufba.softvis.visminer.constant.MetricType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.model.business.Commit;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;

@MetricAnnotation(
		name = "Number of Classes and Interfaces",
		description = "Number of Classes and Interfaces is a software metric used to measure the size of a computer program"+
				" by counting the concrete and abstract classes",
		acronym = "NOCAI",
		type = MetricType.SNAPSHOT,
		uid = MetricUid.NOCAI
	)
public class NOCAIMetric implements IMetric{

	public void calculate(List<AST> astList, List<Commit> commits, MetricPersistance persistence){
		
		Map<Integer, Integer> packageCls = new HashMap<Integer, Integer>();
		int projectQtd = 0;
		for(AST ast : astList){
			
			Document doc = ast.getDocument();
			int id, num = 0;

			if(doc.getTypes() != null){
				num += doc.getTypes().size();
			}
			
			if(doc.getPackageDeclaration() != null){
				
				id = doc.getPackageDeclaration().getId();
				
				if(packageCls.containsKey(id)){
					int aux = packageCls.get(id);
					packageCls.put(id, aux + num);
				}else{
					packageCls.put(id, num);
				}
				
			}

			projectQtd += num;
			
		}
		
		for(Entry<Integer, Integer> entry : packageCls.entrySet()){
			persistence.postMetricValue(entry.getKey(), String.valueOf(entry.getValue()));
		}
		
		persistence.postMetricValue(persistence.getProject().getId(), String.valueOf(projectQtd));
		
	}

}