package org.repositoryminer.codesmell.direct;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;
import org.repositoryminer.codemetric.direct.NOAM;
import org.repositoryminer.codemetric.direct.NOPA;
import org.repositoryminer.codemetric.direct.WMC;
import org.repositoryminer.codemetric.direct.WOC;
import org.repositoryminer.codesmell.CodeSmellId;

public class DataClass implements IDirectCodeSmell {

	private WOC wocMetric = new WOC();
	private NOPA nopaMetric = new NOPA();
	private NOAM noamMetric = new NOAM();
	private WMC wmcMetric = new WMC();

	private float wocThreshold = 1 / 3f;
	private int noamThreshold = 5;
	private int nopaThreshold = 3;
	private int wmcThreshold = 47;

	private int publicMembersThreshold = noamThreshold + nopaThreshold;

	public DataClass() {
	}

	public DataClass(float wocThreshold, int noamThreshold, int nopaThreshold, int wmcThreshold) {
		this.wocThreshold = wocThreshold;
		this.noamThreshold = noamThreshold;
		this.nopaThreshold = nopaThreshold;
		this.wmcThreshold = wmcThreshold;
		publicMembersThreshold = noamThreshold + nopaThreshold;
	}

	@Override
	public Document detect(AbstractClassDeclaration type, AST ast) {
		float woc = wocMetric.calculate(type.getMethods(), type.getFields());
		int nopa = nopaMetric.calculate(type.getFields());
		int noam = noamMetric.calculate(type.getMethods(), type.getFields());
		int wmc = wmcMetric.calculate(type.getMethods());

		if (detect(woc, nopa, noam, wmc)) {
			Document metrics = new Document();
			metrics.append(CodeMetricId.WOC.toString(), woc);
			metrics.append(CodeMetricId.NOPA.toString(), nopa);
			metrics.append(CodeMetricId.NOAM.toString(), noam);
			metrics.append(CodeMetricId.WMC.toString(), wmc);

			return new Document("codesmell", CodeSmellId.DATA_CLASS.toString()).append("metrics", metrics);
		}

		return null;
	}

	@Override
	public CodeSmellId getId() {
		return CodeSmellId.DATA_CLASS;
	}

	@Override
	public Document getThresholds() {
		Document doc = new Document();
		doc.append(CodeMetricId.WOC.toString(), wocThreshold);
		doc.append(CodeMetricId.NOPA.toString(), nopaThreshold);
		doc.append(CodeMetricId.NOAM.toString(), noamThreshold);
		doc.append(CodeMetricId.WMC.toString(), wmcThreshold);

		return new Document("codesmell", CodeSmellId.DATA_CLASS.toString()).append("thresholds", doc);
	}

	private boolean detect(float woc, int nopa, int noam, int wmc) {
		int publicMembers = nopa + noam;

		boolean offerManyData = woc < wocThreshold;
		boolean isNotComplex = (publicMembers > publicMembersThreshold && wmc < wmcThreshold)
				|| (publicMembers > 2 * publicMembersThreshold && wmc < 2 * wmc);
		
		return offerManyData && isNotComplex;
	}

}