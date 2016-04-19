package org.repositoryminer.antipattern;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.DeclarationType;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.metric.ATFDMetric;
import org.repositoryminer.metric.NOAMetric;
import org.repositoryminer.metric.TCCMetric;
import org.repositoryminer.metric.WMCMetric;

public class GodClass implements IAntiPattern {

	private int atfdThreshold = 40;
	private int wmcThreshold = 75;
	private float tccThreshold = 0.2f;
	private int noaThreshold = 20;

	public GodClass() {
	};

	public GodClass(int atfdThreshold, int wmcThreshold, float tccThreshold, int noaThreshold) {
		this.atfdThreshold = atfdThreshold;
		this.wmcThreshold = wmcThreshold;
		this.tccThreshold = tccThreshold;
		this.noaThreshold = noaThreshold;
	}

	@Override
	public void detect(AbstractTypeDeclaration type, AST ast, Document document) {
		if (type.getType() == DeclarationType.CLASS_OR_INTERFACE) {
			TypeDeclaration cls = (TypeDeclaration) type;

			ATFDMetric atfdMetric = new ATFDMetric();
			WMCMetric wmcMetric = new WMCMetric();
			TCCMetric tccMetric = new TCCMetric();
			NOAMetric noaMetric = new NOAMetric();

			int atfd = atfdMetric.calculate(type, cls.getMethods(), false);
			float tcc = tccMetric.calculate(type, cls.getMethods());
			int wmc = wmcMetric.calculate(cls.getMethods());
			int noa = noaMetric.calculate(cls.getFields());

			boolean godClass = (atfd > atfdThreshold)
					&& ((wmc > wmcThreshold) || ((tcc < tccThreshold) && (noa > noaThreshold)));

			document.append("name", new String("God Class")).append("value", new Boolean(godClass));
		}
	}

	public boolean detect(Document document) {
		boolean godClass = false;

		return godClass;
	}
}
