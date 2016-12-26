package org.repositoryminer.codesmell.clazz;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.codesmell.CodeSmellId;
import org.repositoryminer.metric.MetricId;
import org.repositoryminer.metric.clazz.MLOC;
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
 */
public class BrainClass implements IClassCodeSmell {

	private WMC wmcMetric;
	private BrainMethod brainMethod;
	private TCC tccMetric;
	private MLOC mlocMetric;

	private int wmcThreshold = 47;
	private float tccThreshold = 0.5f;
	private int nbmThreshold = 1;
	private int locThreshold = 197;

	public BrainClass() {
		wmcMetric = new WMC();
		brainMethod = new BrainMethod();
		tccMetric = new TCC();
		mlocMetric = new MLOC();
	}

	public BrainClass(int wmcThreshold, float tccThreshold, int nbmThreshold, int locThreshold) {
		this();
		this.wmcThreshold = wmcThreshold;
		this.tccThreshold = tccThreshold;
		this.nbmThreshold = nbmThreshold;
		this.locThreshold = locThreshold;
	}

	@Override
	public CodeSmellId getId() {
		return CodeSmellId.BRAIN_CLASS;
	}

	@Override
	public Document detect(AbstractTypeDeclaration type, AST ast) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			TypeDeclaration cls = (TypeDeclaration) type;
			if (detect(ast, type, cls)) {
				return new Document("name", new String(CodeSmellId.BRAIN_CLASS.toString()));
			}
		}
		return null;
	}

	public boolean detect(AST ast, AbstractTypeDeclaration type, TypeDeclaration cls) {
		int wmc = wmcMetric.calculate(cls.getMethods());
		float tcc = tccMetric.calculate(type, cls.getMethods());

		int nbm = 0; // number of brain methods
		int totalMloc = 0; // total number of lines of code from methods

		for (MethodDeclaration method : cls.getMethods()) {
			totalMloc += mlocMetric.calculate(method, ast);
			if (brainMethod.detect(type, method, ast)) {
				nbm++;
			}
		}

		// Class contains more than one Brain Method and is very large
		boolean exp1 = nbm > nbmThreshold && totalMloc >= locThreshold;

		// Class contains only one BrainMethod but is extremely large and complex
		boolean exp2 = nbm == nbmThreshold && totalMloc >= (2 * locThreshold) && wmc >= (2 * wmcThreshold);

		// Class is very complex and non-cohesive
		boolean exp3 = wmc >= wmcThreshold && tcc < tccThreshold;

		return (exp1 || exp2) && exp3;
	}

	@Override
	public Document getThresholds() {
		Document doc = new Document();
		doc.append(MetricId.WMC.toString(), wmcThreshold);
		doc.append(MetricId.TCC.toString(), tccThreshold);
		doc.append(MetricId.LOC.toString(), locThreshold);
		doc.append(CodeSmellId.BRAIN_METHOD.toString(), nbmThreshold);

		return doc;
	}

}