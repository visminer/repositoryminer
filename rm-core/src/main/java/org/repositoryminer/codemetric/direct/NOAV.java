package org.repositoryminer.codemetric.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

public class NOAV implements IDirectCodeMetric {

	private LVAR lvarMetric = new LVAR();
	private TCC tccMetric = new TCC(); // TCC and NOAV processes accessed fields the same way

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
		if (method.getModifiers().contains("abstract")) {
			return null;
		}
		return calculate(type, method);
	}

	@Override
	public String getMetric() {
		return "NOAV";
	}

	public int calculate(AbstractType currType, AbstractMethod method) {
		int accessFields = tccMetric.processAccessedFields(currType, method).size();
		int nVar = lvarMetric.calculate(method);
		int nParams = method.getParameters() != null ? method.getParameters().size() : 0;
		return accessFields + nVar + nParams;
	}

}