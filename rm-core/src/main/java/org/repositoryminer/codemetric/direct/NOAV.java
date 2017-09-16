package org.repositoryminer.codemetric.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

public class NOAV implements IDirectCodeMetric {

	private LVAR lvarMetric = new LVAR();

	@Override
	public Object calculateFromFile(AST ast) {
		return null;
	}

	@Override
	public Object calculateFromClass(AST ast, AbstractType type) {
		return null;
	}

	@Override
	public Object calculateFromMethod(AST ast, AbstractType type, AbstractMethod method) {
		return calculate(type, method);
	}

	@Override
	public String getMetric() {
		return "NOAV";
	}

	public int calculate(AbstractType currType, AbstractMethod method) {
		int accessFields = LAA.countAccessedFields(method);
		int nVar = lvarMetric.calculate(method);
		int nParams = method.getParameters().size();
		return accessFields + nVar + nParams;
	}

}