package org.repositoryminer.codemetric.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;

public class AMW implements IDirectCodeMetric {

	private WMC wmcMetric = new WMC();
	
	@Override
	public Object calculateFromFile(AST ast) {
		return null;
	}

	@Override
	public Object calculateFromClass(AST ast, AbstractType type) {
		int wmc = wmcMetric.calculate(type.getMethods());
		int nom = type.getMethods().size();
		return calculate(wmc, nom);
	}

	@Override
	public Object calculateFromMethod(AST ast, AbstractType type, AbstractMethod method) {
		return null;
	}

	@Override
	public String getMetric() {
		return "AMW";
	}

	public float calculate(int wmc, int nom) {
		if (nom == 0) {
			return 0l;
		}
		return wmc * 1l / nom;
	}
	
}
