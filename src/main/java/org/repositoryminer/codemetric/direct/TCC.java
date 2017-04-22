package org.repositoryminer.codemetric.direct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.ast.Statement.NodeType;
import org.repositoryminer.codemetric.CodeMetricId;

/**
 * <h1>Tight Class Cohesion</h1>
 * <p>
 * The relative number of method pairs of a class that access in common at least
 * one attribute of the measured class.
 */
public class TCC implements IDirectCodeMetric {

	@Override
	public CodeMetricId getId() {
		return CodeMetricId.TCC;
	}

	@Override
	public Document calculate(AbstractClassDeclaration type, AST ast) {
		return new Document("metric", CodeMetricId.TCC.toString()).append("value", calculate(type));
	}

	public float calculate(AbstractClassDeclaration type) {
		List<MethodDeclaration> methodList = filterMethods(type.getMethods());
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

		if (npc > 0) {
			return (float) ndc / npc;
		}

		return 0;
	}

	private List<MethodDeclaration> filterMethods(List<MethodDeclaration> methods) {
		List<MethodDeclaration> methodList = new ArrayList<MethodDeclaration>();
		for (MethodDeclaration m : methods) {
			if (!(m.getModifiers().contains("abstract") || m.isConstructor()))
				methodList.add(m);
		}
		return methodList;
	}

	public List<String> processAccessedFields(AbstractClassDeclaration currType, MethodDeclaration method) {
		Set<String> fields = new HashSet<String>();

		for (Statement stmt : method.getStatements()) {
			String exp, type, target;

			if (stmt.getNodeType() == NodeType.FIELD_ACCESS || stmt.getNodeType() == NodeType.METHOD_INVOCATION) {
				exp = stmt.getExpression();
				type = exp.substring(0, exp.lastIndexOf("."));
				target = exp.substring(exp.lastIndexOf(".") + 1);
			} else {
				continue;
			}

			if (currType.getName().equals(type)) {
				if (stmt.getNodeType().equals(NodeType.FIELD_ACCESS)) {
					fields.add(target);
				} else if (stmt.getNodeType().equals(NodeType.METHOD_INVOCATION)) {
					fields.addAll(processGetOrSetOrIs(target, currType));
				}
			}

		}
		
		return new ArrayList<String>(fields);
	}

	private boolean isConnected(List<String> method1, List<String> method2) {
		for (String field : method1) {
			if (method2.contains(field))
				return true;
		}
		return false;
	}

	private Collection<String> processGetOrSetOrIs(String methodInv, AbstractClassDeclaration type) {
		String field;
		List<String> fields = new ArrayList<String>(2);

		if ((methodInv.startsWith("get") || methodInv.startsWith("set"))) {
			field = methodInv.substring(3, methodInv.indexOf('('));
		} else if (methodInv.startsWith("is")) {
			field = methodInv.substring(2, methodInv.indexOf('('));
		} else {
			return fields;
		}

		if (field.length() == 0) {
			return fields;
		}
		
		char c[] = field.toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		String field2 = new String(c);

		for (FieldDeclaration fd : type.getFields()) {
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