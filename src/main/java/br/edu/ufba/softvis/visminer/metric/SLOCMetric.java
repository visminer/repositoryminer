package br.edu.ufba.softvis.visminer.metric;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.constant.MetricType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.model.business.Commit;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;

@MetricAnnotation(
		name = "Source Lines of Code",
		description = "Source lines of code (SLOC), also known as lines of code (LOC), is a software metric"
				+ " used to measure the size of a computer program by counting the number of lines in the text of"
				+ " the program's source code.",
		acronym = "SLOC",
		type = MetricType.COMMIT,
		uid = MetricUid.SLOC
	)
public class SLOCMetric implements IMetric{

	private Pattern pattern;
	
	public SLOCMetric(){
		pattern = Pattern.compile("(\r\n)|(\r)|(\n)");
	}
	
	@Override
	public void calculate(List<AST> astList, List<Commit> commits, MetricPersistance persistence){
		
		for(AST ast : astList){
			persistence.postMetricValue(ast.getDocument().getId(), String.valueOf(count(ast.getSourceCode())));
		}
	}
	
	private int count(String source){
		
		if(source == null || source.length() == 0)
			return 0;
		
		Matcher m = pattern.matcher(source);
		
		int i = 0;
		
		while(m.find())
			i++;
		
		return i;
		
	}

}
