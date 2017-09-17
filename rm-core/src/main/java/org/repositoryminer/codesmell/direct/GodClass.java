package org.repositoryminer.codesmell.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.codemetric.direct.ATFD;
import org.repositoryminer.codemetric.direct.TCC;
import org.repositoryminer.codemetric.direct.WMC;

public class GodClass implements IDirectCodeSmell {

	private ATFD atfdMetric = new ATFD();
	private WMC wmcMetric = new WMC();
	private TCC tccMetric = new TCC();

	private int atfdThreshold = 5;
	private int wmcThreshold = 47;
	private float tccThreshold = 0.33f;

	@Override
	public boolean calculateFromFile(AST ast) {
		return false;
	}

	@Override
	public boolean calculateFromClass(AST ast, AbstractType type) {
		float tcc = tccMetric.calculate(type);
		int wmc = wmcMetric.calculate(type.getMethods());

		for (AbstractMethod method : type.getMethods()) {
			atfdMetric.calculateFromMethod(ast, type, method);
		}
		int atfd = (Integer) atfdMetric.calculateFromClass(null, null);
		
		return detect(atfd, wmc, tcc);
	}

	@Override
	public boolean calculateFromMethod(AST ast, AbstractType type, AbstractMethod method) {
		return false;
	}

	@Override
	public String getCodeSmell() {
		return "GOD CLASS";
	}

	public boolean detect(int atfd, int wmc, float tcc) {
		return (atfd > atfdThreshold) && (wmc >= wmcThreshold) && (tcc < tccThreshold);
	}

}