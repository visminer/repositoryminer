package org.repositoryminer.metric.clazz;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.metric.MetricId;

/**
 * <h1>Method Lines of Code</h1>
 * <p>
 * MLOC is defined as the number of non-blank and non-comment lines inside method bodies.
 */
public class MLOC extends MethodBasedMetricTemplate {
	
	private List<Document> methodsDoc;
	private Pattern pattern;

	public MLOC(){
		pattern = Pattern.compile("(\r\n)|(\r)|(\n)");
	}

	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		
		methodsDoc = new ArrayList<Document>();
		int accumulated = 0;
		
		for(MethodDeclaration method : methods){
			int mloc = calculate(method, ast);
			accumulated += mloc;
			methodsDoc.add(new Document("method", method.getName()).append("value", new Integer(mloc)));
		}
		
		document.append("name", MetricId.MLOC).append("accumulated", new Integer(accumulated)).append("methods", methodsDoc);
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