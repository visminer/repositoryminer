package org.repositoryminer.codesmell.direct;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.ClassDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;
import org.repositoryminer.codemetric.direct.ATFD;
import org.repositoryminer.codemetric.direct.TCC;
import org.repositoryminer.codemetric.direct.WMC;
import org.repositoryminer.codesmell.CodeSmellId;

/**
 * <h1>God Class</h1>
 * <p>
 * This code smell detects classes that tend to centralize the intelligence of a
 * system. A god class performs too much work for its own delegating only minor
 * details for trivial classes or using some data of others. This has a negative
 * impact in reusability and understandability of that part of the system.
 * <p>
 * The expression used to evaluate if a class is affected by god class is:<br>
 * godClass = ATFD > atfdThreshold && WMC >= wmcThreshold && TCC < tccThreshold
 * </i>
 * <p>
 * The parameters used are:
 * <ul>
 * <li>ATFD: access to foreign data</li>
 * <li>WMC: weighted method count</li>
 * <li>TCC: tight class cohesion</li>
 * </ul>
 * The default thresholds used are:
 * <ul>
 * <li>atfdThreshold = 5</li>
 * <li>wmcThreshold = 47</li>
 * <li>tccThreshold = 1/3</li>
 * </ul>
 */
public class GodClass implements IDirectCodeSmell {

	private ATFD atfdMetric = new ATFD();
	private WMC wmcMetric = new WMC();
	private TCC tccMetric = new TCC();

	private int atfdThreshold = 5;
	private int wmcThreshold = 47;
	private float tccThreshold = 1.0f / 3.0f;

	public GodClass() {
	}

	public GodClass(int atfdThreshold, int wmcThreshold, float tccThreshold, int noaThreshold) {
		this.atfdThreshold = atfdThreshold;
		this.wmcThreshold = wmcThreshold;
		this.tccThreshold = tccThreshold;
	}

	@Override
	public CodeSmellId getId() {
		return CodeSmellId.GOD_CLASS;
	}

	@Override
	public Document detect(AbstractClassDeclaration type, AST ast) {
		int atfd = atfdMetric.calculate(type);
		float tcc = tccMetric.calculate(type);
		int wmc = wmcMetric.calculate(type.getMethods());

		if (detect(atfd, wmc, tcc)) {
			Document metrics = new Document();
			metrics.append(CodeMetricId.ATFD.toString(), atfd);
			metrics.append(CodeMetricId.WMC.toString(), wmc);
			metrics.append(CodeMetricId.TCC.toString(), tcc);

			return new Document("codesmell", CodeSmellId.GOD_CLASS.toString()).append("metrics", metrics);
		}
		
		return null;
	}

	public boolean detect(AbstractClassDeclaration type, ClassDeclaration cls) {
		int atfd = atfdMetric.calculate(type);
		float tcc = tccMetric.calculate(type);
		int wmc = wmcMetric.calculate(cls.getMethods());

		return atfd > atfdThreshold && wmc >= wmcThreshold && tcc < tccThreshold;
	}

	public boolean detect(int atfd, int wmc, float tcc) {
		return atfd > atfdThreshold && wmc >= wmcThreshold && tcc < tccThreshold;
	}

	@Override
	public Document getThresholds() {
		Document doc = new Document();
		doc.append(CodeMetricId.ATFD.toString(), atfdThreshold);
		doc.append(CodeMetricId.WMC.toString(), wmcThreshold);
		doc.append(CodeMetricId.TCC.toString(), tccThreshold);

		return new Document("codesmell", CodeSmellId.GOD_CLASS.toString()).append("thresholds", doc);
	}

}