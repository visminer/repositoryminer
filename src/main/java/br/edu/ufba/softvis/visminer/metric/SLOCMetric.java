package br.edu.ufba.softvis.visminer.metric;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.constant.MetricType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;

@MetricAnnotation(
		name = "Source Lines of Code",
		description = "Source lines of code (SLOC), also known as lines of code (LOC), is a software metric"
				+ " used to measure the size of a computer program by counting the number of lines in the text of"
				+ " the program's source code.",
		acronym = "SLOC",
		type = MetricType.SIMPLE,
		uid = MetricUid.SLOC
	)
public class SLOCMetric implements IMetric{

	@Override
	public void calculate(Map<FileDB, AST> filesMap, List<CommitDB> commits,
			MetricPersistance persistence) {

		for(Entry<FileDB, AST> entry : filesMap.entrySet()){

			if(entry.getValue() == null){
				continue;
			}
			
			AST ast = entry.getValue();
			
			persistence.postMetricValue(ast.getDocument().getId(), String.valueOf(count(ast.getSourceCode())));
		}
	}
	
	private int count(String source){
		
		if(source == null || source.length() == 0)
			return 0;
		
		int len = source.length();
		int lines = 1;
		
		for(int i = 0; i < len; i++){
			char c = source.charAt(i);
			if(c == '\r'){
				lines++;
				if(i+1 < len && source.charAt(i+1) == '\n')
					i++;
			}else if(c == '\n'){
				lines++;
			}
		}
		
		return lines;
		
	}

}
