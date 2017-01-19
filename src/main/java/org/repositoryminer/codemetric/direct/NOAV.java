package org.repositoryminer.codemetric.direct;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;

/**
 * <h1>Number of Accessed Variables</h1>
 * <p>
 * NOAV is defined as the total number of variables accessed directly from the
 * measured operation. Variables include parameters, local variables, but also
 * instance variables and global variables.
 */
public class NOAV extends MethodBasedMetricTemplate {

	private List<Document> methodsDoc;
	private LVAR lvarMetric;
	private TCC tccMetric; // TCC and NOAV processes accessed fields the same way
	
	public NOAV() {
		lvarMetric = new LVAR();
		tccMetric = new TCC();
	}
	
	@Override
	public CodeMetricId getId() {
		return CodeMetricId.NOAV;
	}

	@Override
	public Document calculate(AbstractClassDeclaration type, List<MethodDeclaration> methods, AST ast) {
		methodsDoc = new ArrayList<Document>();
		List<MethodDeclaration> filteredMethods = filterMethods(methods);
		
		for (MethodDeclaration method : filteredMethods) {
			methodsDoc.add(new Document("method", method.getName()).append("value", calculate(type, method)));
		}

		return new Document("metric", CodeMetricId.NOAV.toString()).append("methods", methodsDoc);
	}

	public int calculate(AbstractClassDeclaration currType, MethodDeclaration method) {
		int accessFields = tccMetric.processAccessedFields(currType, method).size();
		int nVar = lvarMetric.calculate(method);
		int nParams = method.getParameters() != null ? method.getParameters().size() : 0;
		return accessFields + nVar + nParams;
	}
	
	private List<MethodDeclaration> filterMethods(List<MethodDeclaration> methods) {
		List<MethodDeclaration> methodList = new ArrayList<MethodDeclaration>();
		for (MethodDeclaration m : methods) {
			if (!(m.getModifiers().contains("abstract")))
				methodList.add(m);
		}
		return methodList;
	}

}