package org.repositoryminer.codemetric.direct;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractFieldAccess;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractMethodInvocation;
import org.repositoryminer.ast.AbstractStatement;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.ast.NodeType;

public class TCC implements IDirectCodeMetric {

	@Override
	public Object calculateFromFile(AST ast) {
		return null;
	}

	@Override
	public Object calculateFromClass(AST ast, AbstractType type) {
		return calculate(type);
	}

	@Override
	public Object calculateFromMethod(AST ast, AbstractType type, AbstractMethod method) {
		return null;
	}

	@Override
	public String getMetric() {
		return "TCC";
	}

	public float calculate(AbstractType type) {
		List<AbstractMethod> methodList = new ArrayList<AbstractMethod>();
		for (AbstractMethod m : type.getMethods()) {
			if (!(m.getModifiers().contains("abstract") || m.isConstructor()))
				methodList.add(m);
		}
		
		int n = methodList.size();
		int npc = (n * (n - 1)) / 2;
		int ndc = 0;

		List<List<String>> accessedFieldsByMethods = new ArrayList<List<String>>();
		for (AbstractMethod method : methodList) {
			accessedFieldsByMethods.add(processAccessedFields(type, method));
		}

		for (int i = 0; i < accessedFieldsByMethods.size(); i++) {
			for (int j = i + 1; j < accessedFieldsByMethods.size(); j++) {
				if (isConnected(accessedFieldsByMethods.get(i), accessedFieldsByMethods.get(j))) {
					ndc++;
				}
			}
		}

		return npc > 0 ? ndc * 1.0f / npc : 0;
	}

	public List<String> processAccessedFields(AbstractType currType, AbstractMethod method) {
		Set<String> fields = new HashSet<String>();

		for (AbstractStatement stmt : method.getStatements()) {
			if (stmt.getNodeType() == NodeType.FIELD_ACCESS) {
				AbstractFieldAccess fdAccess = (AbstractFieldAccess) stmt;
				if (currType.getName().equals(fdAccess.getDeclaringClass())) {
					fields.add(stmt.getExpression());
				}
			} else if (stmt.getNodeType() == NodeType.METHOD_INVOCATION) {
				AbstractMethodInvocation methodInv = (AbstractMethodInvocation) stmt;
				if (methodInv.isAccessor() && currType.getName().equals(methodInv.getDeclaringClass())) {
					fields.add(methodInv.getAccessedField());
				}
			} else {
				continue;
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

}