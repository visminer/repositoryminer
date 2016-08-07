package org.repositoryminer.codesmell.clazz;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.codesmell.CodeSmellId;
import org.repositoryminer.metric.clazz.CYCLO;
import org.repositoryminer.metric.clazz.MLOC;
import org.repositoryminer.metric.clazz.MAXNESTING;
import org.repositoryminer.metric.clazz.NOAV;

/**
 * <h1>Brain Method</h1>
 * <p>
 * This code smell detects methods that has too many functionalities, becoming
 * hard to maintain and understand. Brain methods tend to centralize the
 * functionality of a class.
 * <p>
 * The expression used to evaluate if a method is affected by brain method is:<br>
 * <i>(MLOC > mlocThreshold) && (CYCLO / MLOC >= ccMlocThreshold) && (MAX_NESTING >=
 * maxNestingThreshold && NOAV > noavThreshold)</i>
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
public class BrainMethod implements IClassCodeSmell {

	private List<Document> methodsDoc;
	private int mlocThreshold = 65;
	private float ccMlocThreshold = 0.24f;
	private int maxNestingThreshold = 5;
	private int noavThreshold = 5;

	public BrainMethod() {
	}

	public BrainMethod(int mlocThreshold, float ccMlocThreshold, int maxNestingThreshold, int noavThreshold) {
		this.mlocThreshold = mlocThreshold;
		this.ccMlocThreshold = ccMlocThreshold;
		this.maxNestingThreshold = maxNestingThreshold;
		this.noavThreshold = noavThreshold;
	}

	@Override
	public void detect(AbstractTypeDeclaration type, AST ast, Document document) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			TypeDeclaration cls = (TypeDeclaration) type;

			methodsDoc = new ArrayList<Document>();

			for (MethodDeclaration method : cls.getMethods()) {
				boolean brainMethod = detect(method, ast);
				methodsDoc.add(new Document("method", method.getName()).append("value", new Boolean(brainMethod)));
			}

			document.append("name", CodeSmellId.BRAIN_METHOD).append("methods", methodsDoc);
		}
	}

	public boolean detect(MethodDeclaration method, AST ast) {
		boolean brainMethod = false;

		MLOC mlocMetric = new MLOC();
		CYCLO ccMetric = new CYCLO();
		NOAV noavMetric = new NOAV();
		MAXNESTING mAXNESTINGMetric = new MAXNESTING();

		int cc = ccMetric.calculate(method);
		int mloc = mlocMetric.calculate(method, ast);
		int noav = noavMetric.calculate(method);
		int maxNesting = mAXNESTINGMetric.calculate(method);

		brainMethod = (mloc > mlocThreshold) && (cc / mloc >= ccMlocThreshold)
				&& (maxNesting >= maxNestingThreshold && noav > noavThreshold);

		return brainMethod;
	}

}