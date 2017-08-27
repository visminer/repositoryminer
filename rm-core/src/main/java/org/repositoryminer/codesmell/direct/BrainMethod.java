package org.repositoryminer.codesmell.direct;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;
import org.repositoryminer.codemetric.direct.CYCLO;
import org.repositoryminer.codemetric.direct.MAXNESTING;
import org.repositoryminer.codemetric.direct.MLOC;
import org.repositoryminer.codemetric.direct.NOAV;
import org.repositoryminer.codesmell.CodeSmellId;

/**
 * <h1>Brain Method</h1>
 * <p>
 * This code smell detects methods that has too many functionalities, becoming
 * hard to maintain and understand. Brain methods tend to centralize the
 * functionality of a class.
 * <p>
 * The expression used to evaluate if a method is affected by brain method
 * is:<br>
 * <i>(MLOC > mlocThreshold) && (CYCLO / MLOC >= ccMlocThreshold) &&
 * (MAX_NESTING >= maxNestingThreshold && NOAV > noavThreshold)</i>
 * <p>
 * The parameters used are:
 * <ul>
 * <li>MLOC: source lines of code per method</li>
 * <li>CYCLO: McCabe’s cyclomatic number</li>
 * <li>MAX_NESTING: maximum nesting level</li>
 * <li>NOAV: number of accessed variables</li>
 * </ul>
 * The default thresholds used are:
 * <ul>
 * <li>mlocThreshold = 65</li>
 * <li>ccMlocThreshold = 0.24</li>
 * <li>maxNestingThreshold = 5</li>
 * <li>noavThreshold = 5</li>
 * </ul>
 */
public class BrainMethod implements IDirectCodeSmell {

	private MLOC mlocMetric = new MLOC();
	private CYCLO ccMetric = new CYCLO();
	private NOAV noavMetric = new NOAV();
	private MAXNESTING maxNestingMetric = new MAXNESTING();

	private int mlocThreshold = 65;
	private float ccThreshold = 10;
	private int maxNestingThreshold = 5;
	private int noavThreshold = 5;

	public BrainMethod() {
	}

	public BrainMethod(int mlocThreshold, float ccMlocThreshold, int maxNestingThreshold, int noavThreshold) {
		this.mlocThreshold = mlocThreshold;
		this.ccThreshold = ccMlocThreshold;
		this.maxNestingThreshold = maxNestingThreshold;
		this.noavThreshold = noavThreshold;
	}

	@Override
	public CodeSmellId getId() {
		return CodeSmellId.BRAIN_METHOD;
	}

	@Override
	public Document detect(AbstractClassDeclaration type, AST ast) {
		List<Document> methods = new ArrayList<Document>();

		for (MethodDeclaration method : type.getMethods()) {
			int cc = ccMetric.calculate(method);
			int mloc = mlocMetric.calculate(method, ast);
			int noav = noavMetric.calculate(type, method);
			int maxNesting = maxNestingMetric.calculate(method);

			if (detect(cc, mloc, noav, maxNesting)) {
				Document metrics = new Document(CodeMetricId.MLOC.toString(), mloc)
						.append(CodeMetricId.CYCLO.toString(), cc).append(CodeMetricId.NOAV.toString(), noav)
						.append(CodeMetricId.MAXNESTING.toString(), maxNesting);
				Document mDoc = new Document("signature", method.getName()).append("metrics", metrics);
				methods.add(mDoc);
			}
		}

		if (methods.size() > 0) {
			return new Document("codesmell", CodeSmellId.BRAIN_METHOD.toString()).append("methods", methods);
		}
		
		return null;
	}

	public boolean detect(int cc, int mloc, int noav, int maxNesting) {
		return mloc > (mlocThreshold / 2) && cc >= ccThreshold && maxNesting >= maxNestingThreshold
				&& noav > noavThreshold;
	}

	public boolean detect(AbstractClassDeclaration type, MethodDeclaration method, AST ast) {
		int cc = ccMetric.calculate(method);
		int mloc = mlocMetric.calculate(method, ast);
		int noav = noavMetric.calculate(type, method);
		int maxNesting = maxNestingMetric.calculate(method);

		return mloc > (mlocThreshold / 2) && cc >= ccThreshold && maxNesting >= maxNestingThreshold
				&& noav > noavThreshold;
	}

	@Override
	public Document getThresholds() {
		Document doc = new Document();
		doc.append(CodeMetricId.MLOC.toString(), mlocThreshold);
		doc.append(CodeMetricId.CYCLO.toString(), ccThreshold);
		doc.append(CodeMetricId.NOAV.toString(), noavThreshold);
		doc.append(CodeMetricId.MAXNESTING.toString(), maxNestingThreshold);

		return new Document("codesmell", CodeSmellId.BRAIN_METHOD.toString()).append("thresholds", doc);
	}

}