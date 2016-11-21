package org.repositoryminer.codesmell.clazz;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.codesmell.CodeSmellId;
import org.repositoryminer.metric.clazz.ATFD;
import org.repositoryminer.metric.clazz.TCC;
import org.repositoryminer.metric.clazz.WMC;

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
 * <b>P.S.</b> The "useNoa" is a flag to define if the NOA metric will be
 * considered or not. The default value of "useNoa" is false.
 */
public class GodClass implements IClassCodeSmell {

	private ATFD atfdMetric;
	private WMC wmcMetric;
	private TCC tccMetric;

	private int atfdThreshold = 5;
	private int wmcThreshold = 47;
	private float tccThreshold = 1.0f / 3.0f;

	public GodClass() {
		atfdMetric = new ATFD();
		wmcMetric = new WMC();
		tccMetric = new TCC();
	}

	public GodClass(int atfdThreshold, int wmcThreshold, float tccThreshold, int noaThreshold, boolean useNoa) {
		this.atfdThreshold = atfdThreshold;
		this.wmcThreshold = wmcThreshold;
		this.tccThreshold = tccThreshold;
	}

	@Override
	public String getId() {
		return CodeSmellId.GOD_CLASS;
	}

	@Override
	public void detect(AbstractTypeDeclaration type, AST ast, Document document) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			TypeDeclaration cls = (TypeDeclaration) type;
			document.append("name", CodeSmellId.GOD_CLASS).append("value", detect(type, cls));
		}
	}

	public boolean detect(AbstractTypeDeclaration type, TypeDeclaration cls) {
		atfdMetric = new ATFD();
		wmcMetric = new WMC();
		tccMetric = new TCC();

		int atfd = atfdMetric.calculate(type, cls.getMethods(), false);
		float tcc = tccMetric.calculate(type, cls.getMethods());
		int wmc = wmcMetric.calculate(cls.getMethods());

		return atfd > atfdThreshold && wmc >= wmcThreshold && tcc < tccThreshold;
	}

}