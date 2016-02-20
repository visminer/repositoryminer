package br.edu.ufba.softvis.visminer.metric;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.FieldDeclaration;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.ast.Statement;
import br.edu.ufba.softvis.visminer.ast.TypeDeclaration;
import br.edu.ufba.softvis.visminer.constant.NodeType;

@MetricAnnotation(name = "Tight Class Cohesion", description = "Tight Class Cohesion (TCC) measures the cohesion of a class "
		+ "as the relative number of directly connected methods, where "
		+ "methods are considered to be connected when they access ,in common, "
		+ "at least one attribute of the measured class.", acronym = "TCC")
public class TCCMetric extends MethodBasedMetricTemplate {

	@Override
	public void calculate(TypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		float tcc = calculate(type, methods);
		
		document.append("TCC", new Document("accumulated", new Float(tcc)));
	}

	public float calculate(TypeDeclaration type, List<MethodDeclaration> methods) {
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

	private List<String> processAccessedFields(TypeDeclaration currType, MethodDeclaration method) {

		List<String> accessedFields = new ArrayList<String>();

		for (Statement stm : method.getStatements()) {
			if (stm.getNodeType().equals(NodeType.FIELD_ACCESS)) {
				String exp = stm.getExpression();
				String type = exp.substring(0, exp.lastIndexOf("."));
				String field = exp.substring(exp.lastIndexOf(".") + 1);
				if (currType.getName().equals(type))
					accessedFields.add(field);
			} else if (stm.getNodeType().equals(NodeType.METHOD_INVOCATION)) {
				String exp = stm.getExpression();
				String type = exp.substring(0, exp.lastIndexOf("."));
				String methodInv = exp.substring(exp.lastIndexOf(".") + 1);
				if (currType.getName().equals(type)) {
					if (isGetterOrSetter(methodInv)) {
						String field = methodInv.substring(3);
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

		if ((methodInv.startsWith("get") || methodInv.startsWith("set")) && methodInv.length() > 3) {
			for (FieldDeclaration fd : currentFields) {
				String field = methodInv.substring(3);
				if (fd.getName().equals(field) || fd.getName().equals(
						Character.toLowerCase(field.charAt(0)) + (field.length() > 1 ? field.substring(1) : ""))) {
					return true;
				}
			}

		}

		return false;
	}

}
