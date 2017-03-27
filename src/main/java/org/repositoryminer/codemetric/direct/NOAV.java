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
public class NOAV implements IDirectCodeMetric {

	private List<Document> methodsDoc = new ArrayList<Document>();
	private LVAR lvarMetric = new LVAR();
	private TCC tccMetric = new TCC(); // TCC and NOAV processes accessed fields the same way
	
	@Override
	public CodeMetricId getId() {
		return CodeMetricId.NOAV;
	}

	@Override
	public Document calculate(AbstractClassDeclaration type, AST ast) {
		methodsDoc.clear();
		List<MethodDeclaration> filteredMethods = filterMethods(type.getMethods());
		
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