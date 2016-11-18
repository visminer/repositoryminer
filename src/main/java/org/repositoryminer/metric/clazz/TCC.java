package org.repositoryminer.metric.clazz;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.ast.Statement.NodeType;
import org.repositoryminer.metric.MetricId;

/**
 * <h1>Tight Class Cohesion</h1>
 * <p>
 * The relative number of method pairs of a class that access in common at least
 * one attribute of the measured class.
 */
public class TCC extends MethodBasedMetricTemplate {

	@Override
	public String getId() {
		return MetricId.TCC;
	}

	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		float tcc = calculate(type, methods);
		document.append("name", MetricId.TCC).append("value", new Float(tcc));
	}

	public float calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods) {
		List<MethodDeclaration> methodList = filterMethods(methods);
		int n = methodList.size();
		int npc = (n * (n - 1)) / 2; // Number of possible connected methods
		int ndc = 0; // number of directly connected methods

		for (int i = 0; i < n; i++) {
			List<String> accessedFieldsMethod1 = processAccessedFields(type, methodList.get(i));
			for (int j = i + 1; j < n; j++) {
				List<String> accessedFieldsMethod2 = processAccessedFields(type, methodList.get(j));
				if (isConnected(accessedFieldsMethod1, accessedFieldsMethod2))
					ndc++;
			}
		}

		float tcc = 0;
		if (npc > 0) {
			tcc = (float) ndc / npc;
		}
		return tcc;
	}

	private List<MethodDeclaration> filterMethods(List<MethodDeclaration> methods) {
		List<MethodDeclaration> methodList = new ArrayList<MethodDeclaration>();
		for (MethodDeclaration m : methods) {
			if (!(m.getModifiers().contains("abstract") || m.isConstructor()))
				methodList.add(m);
		}
		return methodList;
	}

	private List<String> processAccessedFields(AbstractTypeDeclaration currType, MethodDeclaration method) {
		List<String> accessedFields = new ArrayList<String>();
		for (Statement stmt : method.getStatements()) {
			String exp = stmt.getExpression();
			String type = exp.substring(0, exp.lastIndexOf("."));
			String target = exp.substring(exp.lastIndexOf(".") + 1);

			if (currType.getName().equals(type)) {
				if (stmt.getNodeType().equals(NodeType.FIELD_ACCESS)) {
					accessedFields.add(target);
				} else if (stmt.getNodeType().equals(NodeType.METHOD_INVOCATION)) {
					if (isGetterOrSetter(target)) {
						String field = target.substring(3);
						accessedFields.add(Character.toLowerCase(field.charAt(0))
								+ (field.length() > 1 ? field.substring(1) : ""));
						accessedFields.add(field);
					}
				}
			}

		}
		return accessedFields;
	}

	private boolean isConnected(List<String> method1, List<String> method2) {
		for (String field : method1) {
			if (method2.contains(field))
				return true;
		}
		return false;
	}

	private boolean isGetterOrSetter(String methodInv) {
		String field;

		if ((methodInv.startsWith("get") || methodInv.startsWith("set")) && methodInv.length() > 3) {
			field = methodInv.substring(3);
		} else if (methodInv.startsWith("is") && methodInv.length() > 2) {
			field = methodInv.substring(2);
		} else {
			return false;
		}
		
		for (FieldDeclaration fd : currentFields) {
			if (fd.getName().equals(field) || fd.getName()
					.equals(Character.toLowerCase(field.charAt(0)) + (field.length() > 1 ? field.substring(1) : ""))) {
				return true;
			}
		}
		
		return false;
	}

}