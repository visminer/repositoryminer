package org.repositoryminer.codemetric.direct;

import java.util.List;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

public class WMC implements IDirectCodeMetric {

	private CYCLO cc = new CYCLO();

	@Override
	public Object calculateFromFile(AST ast) {
		return null;
	}

	@Override
	public Object calculateFromClass(AST ast, AbstractType type) {
		// TODO Auto-generated method stub
		return calculate(type.getMethods());
	}

	@Override
	public Object calculateFromMethod(AST ast, AbstractType type, AbstractMethod method) {
		return null;
	}

	@Override
	public String getMetric() {
		return "WMC";
	}

	public int calculate(List<AbstractMethod> methods) {
		int wmc = 0;
		for (AbstractMethod method : methods) {
			wmc += cc.calculate(method);
		}
		return wmc;
	}

}
