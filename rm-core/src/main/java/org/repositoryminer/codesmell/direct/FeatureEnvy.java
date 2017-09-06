package org.repositoryminer.codesmell.direct;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.codemetric.direct.ATFD;
import org.repositoryminer.codemetric.direct.FDP;
import org.repositoryminer.codemetric.direct.LAA;

public class FeatureEnvy implements IDirectCodeSmell {

	private float laaThreshold = 0.33f;
	private int atfdThreshold = 5;
	private int fdpThreshold = 5;
	
	private LAA laaMetric = new LAA();
	private ATFD atfdMetric = new ATFD();
	private FDP fdpMetric = new FDP();
	
	@Override
	public boolean calculateFromFile(AST ast) {
		return false;
	}

	@Override
	public boolean calculateFromClass(AST ast, AbstractType type) {
		return false;
	}

	@Override
	public boolean calculateFromMethod(AST ast, AbstractType type, AbstractMethod method) {
		float laaValue = laaMetric.calculate(type, method);
		int fdpValue = fdpMetric.calculate(type, method);
		int atfdValue = atfdMetric.calculate(type, method);
		return detect(laaValue, atfdValue, fdpValue);
	}

	@Override
	public String getCodeSmell() {
		return "FEATURE ENVY";
	}

	/*
	 * Detection Strategy for Feature Envy
	 * (ATFD > FEW) AND (LAA < ONE THIRD) AND (FDP <= FEW)
	 */
	private boolean detect(float laaValue, int atfdValue, int fdpValue) {
		return (atfdValue > atfdThreshold) && (laaValue < laaThreshold) && (fdpValue <= fdpThreshold);
	}
	
}