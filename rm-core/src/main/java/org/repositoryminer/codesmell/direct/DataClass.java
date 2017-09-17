package org.repositoryminer.codesmell.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.codemetric.direct.NOAM;
import org.repositoryminer.codemetric.direct.NOPA;
import org.repositoryminer.codemetric.direct.WMC;
import org.repositoryminer.codemetric.direct.WOC;

public class DataClass implements IDirectCodeSmell {

	private WOC wocMetric = new WOC();
	private NOPA nopaMetric = new NOPA();
	private NOAM noamMetric = new NOAM();
	private WMC wmcMetric = new WMC();

	private float wocThreshold = 0.33f;
	private int wmcThreshold1 = 31;
	private int wmcThreshold2 = 47;
	private int publicMembersThreshold1 = 5;
	private int publicMembersThreshold2 = 8;
	
	@Override
	public boolean calculateFromFile(AST ast) {
		return false;
	}

	@Override
	public boolean calculateFromClass(AST ast, AbstractType type) {
		float woc = (Float) wocMetric.calculateFromClass(ast, type);
		int nopa = (Integer) nopaMetric.calculateFromClass(ast, type);
		int noam = (Integer) noamMetric.calculateFromClass(ast, type);
		int wmc = (Integer) wmcMetric.calculateFromClass(ast, type);
		return detect(woc, nopa, noam, wmc);
	}

	@Override
	public boolean calculateFromMethod(AST ast, AbstractType type, AbstractMethod method) {
		return false;
	}

	@Override
	public String getCodeSmell() {
		return "DATA CLASS";
	}

	private boolean detect(float woc, int nopa, int noam, int wmc) {
		int publicMembers = nopa + noam;
		boolean offerManyData = woc < wocThreshold;
		boolean isNotComplex = (publicMembers > publicMembersThreshold1 && wmc < wmcThreshold1)
				|| (publicMembers > publicMembersThreshold2 && wmc < wmcThreshold2);
		return offerManyData && isNotComplex;
	}
	
}
