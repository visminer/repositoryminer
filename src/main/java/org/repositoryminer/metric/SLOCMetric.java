package org.repositoryminer.metric;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.listener.IMetricCalculationListener;

public class SLOCMetric implements ICommitMetric{

	private Pattern pattern;

	public SLOCMetric(){
		pattern = Pattern.compile("(\r\n)|(\r)|(\n)");
	}

	@Override
	public void calculate(AbstractTypeDeclaration type, AST ast, IMetricCalculationListener listener) {
		int sloc = calculate(ast.getSourceCode());
		
		listener.updateMetricValue(SLOC, sloc);
	}
	
	public int calculate(String source){
		if(source == null || source.length() == 0)
			return 0;

		Matcher m = pattern.matcher(source);

		//starts from 1 because the matcher doesn't count the last line (only line breaks)
		int i = 1;

		while(m.find())
			i++;

		return i;
	}

}