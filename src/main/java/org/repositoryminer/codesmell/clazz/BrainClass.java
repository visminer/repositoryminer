package org.repositoryminer.codesmell.clazz;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.codesmell.CodeSmellId;
import org.repositoryminer.metric.clazz.LOC;
import org.repositoryminer.metric.clazz.TCC;
import org.repositoryminer.metric.clazz.WMC;

/**
 * <h1>Brain Class</h1>
 * <p>
 * This code smell is about too complex classes that tend accumulate an
 * excessive amount of intelligence, usually constituted with several methods
 * affected by Brain Method.
 * <p>
 * This code smell recalls God Class, because those classes also have the
 * tendency to centralize intelligence of the system. The two disharmonies looks
 * like quite similar, this is partially true because they refer to complex
 * classes. But the two problems are distinct.
 * <p>
 * The expression used to evaluate if a class is affected by Brain Class is:<br>
 * <i>IS_GOD_CLASS && (WMC >= wmcThreshold && TCC < tccThreshold) && ((NBM >
 * nbmThreshold && LOC >= locThreshold) || (NBM == nbmThreshold && LOC >=
 * (2 * locThreshold) && WMC >= (2 * wmcThreshold)))</i>
 * <p>
 * The parameters used are:
 * <ul>
 * <li>IS_GOD_CLASS: true if the class is a god class or false otherwise</li>
 * <li>WMC: weighted methods per class</li>
 * <li>TCC: tight class cohesion</li>
 * <li>NBM: number of brain methods</li>
 * <li>LOC: lines of code</li>
 * </ul>
 * The default thresholds used are:
 * <ul>
 * <li>wmcThreshold = 47</li>
 * <li>tccThreshold = 0.5</li>
 * <li>nbmThreshold = 1</li>
 * <li>locThreshold = 197</li>
 * </ul>
 * <p>
 */
public class BrainClass implements IClassCodeSmell {

	private int wmcThreshold = 47;
	private float tccThreshold = 0.5f;
	private int nbmThreshold = 1;
	private int locThreshold = 197;

	public BrainClass() {
	}

	public BrainClass(int wmcThreshold, float tccThreshold, int nbmThreshold, int locThreshold) {
		this.wmcThreshold = wmcThreshold;
		this.tccThreshold = tccThreshold;
		this.nbmThreshold = nbmThreshold;
		this.locThreshold = locThreshold;
	}

	@Override
	public void detect(AbstractTypeDeclaration type, AST ast, Document document) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			TypeDeclaration cls = (TypeDeclaration) type;
			boolean brainClass = detect(ast, type, cls);
			document.append("name", new String(CodeSmellId.BRAIN_CLASS)).append("value", new Boolean(brainClass));
		}
	}

	public boolean detect(AST ast, AbstractTypeDeclaration type, TypeDeclaration cls) {
		boolean brainClass = false;
		WMC wmcMetric = new WMC();
		GodClass godClass = new GodClass();
		BrainMethod brainMethod = new BrainMethod();
		TCC tccMetric = new TCC();
		LOC locMetric = new LOC();

		int wmc = wmcMetric.calculate(cls.getMethods());
		boolean isGodClass = godClass.detect(type, cls);
		int nbm = 0; // number of brain methods
		float tcc = tccMetric.calculate(type, cls.getMethods());
		int loc = locMetric.calculate(ast.getSourceCode());

		for (MethodDeclaration method : cls.getMethods()) {
			if (brainMethod.detect(method, ast))
				nbm++;
		}

		brainClass = isGodClass && (wmc >= wmcThreshold && tcc < tccThreshold)
				&& ((nbm > nbmThreshold && loc >= locThreshold)
						|| (nbm == nbmThreshold && loc >= (2 * locThreshold) && wmc >= (2 * wmcThreshold)));

		return brainClass;
	}

}