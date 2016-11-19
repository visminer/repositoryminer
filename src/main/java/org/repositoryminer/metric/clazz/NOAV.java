package org.repositoryminer.metric.clazz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.ast.Statement.NodeType;
import org.repositoryminer.metric.MetricId;

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
	
	@Override
	public String getId() {
		return MetricId.NOAV;
	}

	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		methodsDoc = new ArrayList<Document>();
		List<MethodDeclaration> filteredMethods = filterMethods(methods);
		
		for (MethodDeclaration method : filteredMethods) {
			int noav = calculate(type, method);
			methodsDoc.add(new Document("method", method.getName()).append("value", new Integer(noav)));
		}

		document.append("name", MetricId.NOAV).append("methods", methodsDoc);
	}

	public int calculate(AbstractTypeDeclaration currType, MethodDeclaration method) {
		int accessFields = processAccessedFields(currType, method);
		int nVar = lvarMetric.calculate(method);
		int nParams = method.getParameters() != null ? method.getParameters().size() : 0;
		
		return accessFields + nVar + nParams;
	}
	
	public List<MethodDeclaration> filterMethods(List<MethodDeclaration> methods) {
		List<MethodDeclaration> methodList = new ArrayList<MethodDeclaration>();
		for (MethodDeclaration m : methods) {
			if (!(m.getModifiers().contains("abstract")))
				methodList.add(m);
		}
		return methodList;
	}

	public int processAccessedFields(AbstractTypeDeclaration currType, MethodDeclaration method) {
		Set<String> fields = new HashSet<String>();
		
		for (Statement stmt : method.getStatements()) {
			String exp = stmt.getExpression();
			String type = exp.substring(0, exp.lastIndexOf("."));
			String target = exp.substring(exp.lastIndexOf(".") + 1);

			if (currType.getName().equals(type)) {
				if (stmt.getNodeType().equals(NodeType.FIELD_ACCESS)) {
					fields.add(target);
				} else if (stmt.getNodeType().equals(NodeType.METHOD_INVOCATION)) {
					fields.addAll(processGetOrSetOrIs(target));
				}
			}

		}
		
		return fields.size();
	}
	
	private Collection<String> processGetOrSetOrIs(String methodInv) {
		String field;
		List<String> fields = new ArrayList<String>(2);
		
		if ((methodInv.startsWith("get") || methodInv.startsWith("set")) && methodInv.length() > 3) {
			field = methodInv.substring(3);
		} else if (methodInv.startsWith("is") && methodInv.length() > 2) {
			field = methodInv.substring(2);
		} else {
			return fields;
		}
		
		char c[] = field.toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		String field2 = new String(c);
		
		for (FieldDeclaration fd : currentFields) {
			if (fd.getName().equals(field)) {
				fields.add(field);
			} else if (fd.getName().equals(field2)) {
				fields.add(field2);
			} else if (fields.size() == 2) {
				break;
			}
		}
		
		return fields;
	}
	
}