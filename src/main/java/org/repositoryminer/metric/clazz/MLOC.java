package org.repositoryminer.metric.clazz;

import java.util.ArrayList;
import java.util.List;

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
	private LOC locMetric;

	public MLOC() {
		locMetric = new LOC();
	}
	
	@Override
	public MetricId getId() {
		return MetricId.MLOC;
	}
	
	@Override
	public Document calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast) {
		methodsDoc = new ArrayList<Document>();
		int accumulated = 0;
		
		for(MethodDeclaration method : methods){
			int mloc = calculate(method, ast);
			accumulated += mloc;
			methodsDoc.add(new Document("method", method.getName()).append("value", new Integer(mloc)));
		}
		
		return new Document("name", MetricId.MLOC.toString()).append("accumulated", new Integer(accumulated)).append("methods", methodsDoc);
	}
	
	public int calculate(MethodDeclaration method, AST ast){
		String sourceCode = ast.getSourceCode();
		String methodSourceCode = sourceCode.substring(method.getStartPositionInSourceCode(), method.getEndPositionInSourceCode());
		return locMetric.calculate(methodSourceCode);
	}

}