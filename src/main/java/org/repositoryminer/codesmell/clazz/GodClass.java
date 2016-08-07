package org.repositoryminer.codesmell.clazz;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.codesmell.CodeSmellId;
import org.repositoryminer.metric.clazz.ATFD;
import org.repositoryminer.metric.clazz.NOA;
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
 * <i> if (useNoa)<br>
 * godClass = (ATFD > atfdThreshold) && ((WMC > wmcThreshold) || ((TCC <
 * tccThreshold) && (NOA > noaThreshold)))<br>
 * else<br>
 * godClass = (ATFD > atfdThreshold) && ((WMC > wmcThreshold) || ((TCC <
 * tccThreshold))) </i>
 * <p>
 * The parameters used are:
 * <ul>
 * <li>ATFD: access to foreign data</li>
 * <li>WMC: weighted method count</li>
 * <li>TCC: tight class cohesion</li>
 * <li>NOA: number of attributes</li>
 * </ul>
 * The default thresholds used are:
 * <ul>
 * <li>atfdThreshold = 40</li>
 * <li>wmcThreshold = 75</li>
 * <li>tccThreshold = 0.2</li>
 * <li>noaThreshold = 20</li>
 * </ul>
 * <b>P.S.</b> The "useNoa" is a flag to define if the NOA metric will be
 * considered or not. The default value of "useNoa" is false.
 */
public class GodClass implements IClassCodeSmell {

	private int atfdThreshold = 40;
	private int wmcThreshold = 75;
	private float tccThreshold = 0.2f;
	private int noaThreshold = 20;
	private boolean useNoa = false;

	public GodClass() {
	}

	public GodClass(int atfdThreshold, int wmcThreshold, float tccThreshold, int noaThreshold, boolean useNoa) {
		this.atfdThreshold = atfdThreshold;
		this.wmcThreshold = wmcThreshold;
		this.tccThreshold = tccThreshold;
		this.noaThreshold = noaThreshold;
		this.useNoa = useNoa;
	}

	@Override
	public void detect(AbstractTypeDeclaration type, AST ast, Document document) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			TypeDeclaration cls = (TypeDeclaration) type;

			boolean godClass = detect(type, cls);
			document.append("name", CodeSmellId.GOD_CLASS).append("value", new Boolean(godClass));
		}
	}

	public boolean detect(AbstractTypeDeclaration type, TypeDeclaration cls) {
		boolean godClass = false;

		ATFD atfdMetric = new ATFD();
		WMC wmcMetric = new WMC();
		TCC tccMetric = new TCC();
		NOA noaMetric = new NOA();

		int atfd = atfdMetric.calculate(type, cls.getMethods(), false);
		float tcc = tccMetric.calculate(type, cls.getMethods());
		int wmc = wmcMetric.calculate(cls.getMethods());
		int noa = noaMetric.calculate(cls.getFields());

		if (useNoa)
			godClass = (atfd > atfdThreshold)
					&& ((wmc > wmcThreshold) || ((tcc < tccThreshold) && (noa > noaThreshold)));
		else
			godClass = (atfd > atfdThreshold) && ((wmc > wmcThreshold) || ((tcc < tccThreshold)));

		return godClass;
	}
	
}