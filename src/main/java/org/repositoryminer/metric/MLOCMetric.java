package org.repositoryminer.metric;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.listener.IMetricCalculationListener;

public class MLOCMetric extends MethodBasedMetricTemplate {
	
	private Pattern pattern;

	public MLOCMetric(){
		pattern = Pattern.compile("(\r\n)|(\r)|(\n)");
	}

	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, 
			IMetricCalculationListener listener) {
		
		Map<String, Integer> valuesPerMethod = new HashMap<String, Integer>();
		int accumulated = 0;
		
		for(MethodDeclaration method : methods){
			int mloc = calculate(method, ast);
			accumulated += mloc;
			valuesPerMethod.put(method.getName(), new Integer(mloc));
		}
		
		listener.updateMethodBasedMetricValue(MLOC, accumulated, valuesPerMethod);
	}
	
	
	public int calculate(MethodDeclaration method, AST ast){
		String sourceCode = ast.getSourceCode();
		String methodSourceCode = sourceCode.substring(method.getStartPositionInSourceCode(), method.getEndPositionInSourceCode());

		if(methodSourceCode == null || methodSourceCode.length() == 0)
			return 0;

		//TODO ignore blank lines and comments
		Matcher m = pattern.matcher(methodSourceCode);

		//starts from 1 because the matcher doesn't count the last line (only line breaks)
		int i = 1;

		while(m.find())
			i++;

		return i;
	}

}