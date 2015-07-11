package br.edu.ufba.softvis.visminer.metric;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.Document;
import br.edu.ufba.softvis.visminer.ast.TypeDeclaration;
import br.edu.ufba.softvis.visminer.constant.MetricType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.model.bean.Commit;
import br.edu.ufba.softvis.visminer.model.bean.File;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;

@MetricAnnotation(
	name = "Number of Methods",
	description = "Number of Methods is a software metric used to count the number of Methods per classes",
	acronym = "NOM",
	type = MetricType.SIMPLE,
	uid = MetricUid.NOM
)
public class NOMMetric implements IMetric {

	@Override
	public void calculate(Map<File, AST> filesMap, List<Commit> commits, MetricPersistance persistence){

		for(Entry<File, AST> entry : filesMap.entrySet()){

			AST ast = entry.getValue();
			
			if(ast == null || ast.getDocument().getTypesDeclarations() == null){
				continue;
			}

			Document doc = entry.getValue().getDocument();
			for(TypeDeclaration type : doc.getTypesDeclarations()){
				
				int m = 0;
				if(type.getMethods() != null){
					m = type.getMethods().size();
				}
				persistence.saveMetricValue(type.getId(), String.valueOf(m));
			}
			
		}
		
	}

}
