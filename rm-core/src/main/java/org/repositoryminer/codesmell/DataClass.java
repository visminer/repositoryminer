package org.repositoryminer.codesmell;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractType;
import org.repositoryminer.metric.MetricID;

public class DataClass implements ICodeSmell {

	private static final MetricID[] REQUIRED_METRICS = { MetricID.WOC, MetricID.NOPA, MetricID.NOAM, MetricID.WMC };

	private float wocThreshold = 0.33f;
	private int wmcThreshold1 = 31;
	private int wmcThreshold2 = 47;
	private int publicMembersThreshold1 = 5;
	private int publicMembersThreshold2 = 8;

	public DataClass() {
	}

	public DataClass(float wocThreshold, int wmcThreshold1, int wmcThreshold2, int publicMembersThreshold1,
			int publicMembersThreshold2) {
		this.wocThreshold = wocThreshold;
		this.wmcThreshold1 = wmcThreshold1;
		this.wmcThreshold2 = wmcThreshold2;
		this.publicMembersThreshold1 = publicMembersThreshold1;
		this.publicMembersThreshold2 = publicMembersThreshold2;
	}

	@Override
	public void detect(AST ast) {
		for (AbstractType type : ast.getTypes()) {
			float woc = (Float) type.getMetrics().get(MetricID.WOC);
			int nopa = (Integer) type.getMetrics().get(MetricID.NOPA);
			int noam = (Integer) type.getMetrics().get(MetricID.NOAM);
			int wmc = (Integer) type.getMetrics().get(MetricID.WMC);
			if (detect(woc, nopa, noam, wmc)) {
				type.getCodeSmells().add(CodeSmellID.DATA_CLASS);
			}
		}
	}

	private boolean detect(float woc, int nopa, int noam, int wmc) {
		int publicMembers = nopa + noam;
		boolean offerManyData = woc < wocThreshold;
		boolean isNotComplex = (publicMembers > publicMembersThreshold1 && wmc < wmcThreshold1)
				|| (publicMembers > publicMembersThreshold2 && wmc < wmcThreshold2);
		return offerManyData && isNotComplex;
	}

	@Override
	public CodeSmellID getId() {
		return CodeSmellID.DATA_CLASS;
	}

	@Override
	public MetricID[] getRequiredMetrics() {
		return REQUIRED_METRICS;
	}

	@Override
	public CodeSmellID[] getRequiredCodeSmells() {
		return null;
	}

	/*** GETTERS AND SETTERS ***/

	public float getWocThreshold() {
		return wocThreshold;
	}

	public void setWocThreshold(float wocThreshold) {
		this.wocThreshold = wocThreshold;
	}

	public int getWmcThreshold1() {
		return wmcThreshold1;
	}

	public void setWmcThreshold1(int wmcThreshold1) {
		this.wmcThreshold1 = wmcThreshold1;
	}

	public int getWmcThreshold2() {
		return wmcThreshold2;
	}

	public void setWmcThreshold2(int wmcThreshold2) {
		this.wmcThreshold2 = wmcThreshold2;
	}

	public int getPublicMembersThreshold1() {
		return publicMembersThreshold1;
	}

	public void setPublicMembersThreshold1(int publicMembersThreshold1) {
		this.publicMembersThreshold1 = publicMembersThreshold1;
	}

	public int getPublicMembersThreshold2() {
		return publicMembersThreshold2;
	}

	public void setPublicMembersThreshold2(int publicMembersThreshold2) {
		this.publicMembersThreshold2 = publicMembersThreshold2;
	}

}