package org.repositoryminer.codesmell.direct;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.AbstractClassDeclaration.Archetype;
import org.repositoryminer.codemetric.CodeMetricId;
import org.repositoryminer.codemetric.direct.MLOC;
import org.repositoryminer.codemetric.direct.TCC;
import org.repositoryminer.codemetric.direct.WMC;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.ClassDeclaration;
import org.repositoryminer.codesmell.CodeSmellId;

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
public class BrainClass implements IDirectCodeSmell {

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
	public Document detect(AbstractClassDeclaration type, AST ast) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			ClassDeclaration cls = (ClassDeclaration) type;
			
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
			
			if (detect(nbm, totalMloc, wmc, tcc)) {
				Document metrics = new Document();
				metrics.append(CodeMetricId.WMC.toString(), wmc);
				metrics.append(CodeMetricId.TCC.toString(), tcc);
				metrics.append(CodeMetricId.LOC.toString(), totalMloc);
				metrics.append(CodeSmellId.BRAIN_METHOD.toString(), nbm);
				
				return new Document("codesmell", new String(CodeSmellId.BRAIN_CLASS.toString())).append("metrics",
						metrics);
			}
		}
		
		return null;
	}

	public boolean detect(int nbm, int totalMloc, int wmc, float tcc) {
		// Class contains more than one Brain Method and is very large
		boolean exp1 = nbm > nbmThreshold && totalMloc >= locThreshold;

		// Class contains only one BrainMethod but is extremely large and
		// complex
		boolean exp2 = nbm == nbmThreshold && totalMloc >= (2 * locThreshold) && wmc >= (2 * wmcThreshold);

		// Class is very complex and non-cohesive
		boolean exp3 = wmc >= wmcThreshold && tcc < tccThreshold;

		return (exp1 || exp2) && exp3;
	}

	@Override
	public Document getThresholds() {
		Document doc = new Document();
		doc.append(CodeMetricId.WMC.toString(), wmcThreshold);
		doc.append(CodeMetricId.TCC.toString(), tccThreshold);
		doc.append(CodeMetricId.LOC.toString(), locThreshold);
		doc.append(CodeSmellId.BRAIN_METHOD.toString(), nbmThreshold);

		return new Document("codesmell", CodeSmellId.BRAIN_CLASS.toString()).append("thresholds", doc);
	}

}