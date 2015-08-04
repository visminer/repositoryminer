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
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;

@MetricAnnotation(
		name = "Number of Classes and Interfaces",
		description = "Number of Classes and Interfaces is a software metric used to measure the size of a computer program"+
				" by counting the concrete and abstract classes",
		acronym = "NOCAI",
		type = MetricType.COMPLEX,
		uid = MetricUid.NOCAI
	)
public class NOCAIMetric implements IMetric{

	@Override
	public void calculate(Map<FileDB, AST> filesMap, List<CommitDB> commits,
			MetricPersistance persistence) {

		Map<Integer, Integer> packageCls = new HashMap<Integer, Integer>();
		
		for(Entry<FileDB, AST> entry : filesMap.entrySet()){
			
			AST ast = entry.getValue();
			
			if(ast == null){
				continue;
			}
			
			Document doc = ast.getDocument();
			int id, num = 0;

			if(doc.getTypes() != null){
				num += doc.getTypes().size();
			}
			
			if(doc.getPackageDeclaration() != null){
				id = doc.getPackageDeclaration().getId();
			}else{
				id = ast.getProject().getId();
			}
			
			if(packageCls.containsKey(id)){
				int aux = packageCls.get(id);
				packageCls.put(id, aux + num);
			}else{
				packageCls.put(id, num);
			}
			
		}
		
		for(Entry<Integer, Integer> entry : packageCls.entrySet()){
			persistence.postMetricValue(entry.getKey(), String.valueOf(entry.getValue()));
		}
		
	}

}