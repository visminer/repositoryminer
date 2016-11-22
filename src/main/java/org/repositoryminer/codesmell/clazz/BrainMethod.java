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
public class BrainMethod implements IClassCodeSmell {

	private List<Document> methodsDoc;
	private MLOC mlocMetric;
	private CYCLO ccMetric;
	private NOAV noavMetric;
	private MAXNESTING maxNestingMetric;

	private int mlocThreshold = 65;
	private float ccThreshold = 0.24f;
	private int maxNestingThreshold = 5;
	private int noavThreshold = 5;

	public BrainMethod() {
		mlocMetric = new MLOC();
		ccMetric = new CYCLO();
		noavMetric = new NOAV();
		maxNestingMetric = new MAXNESTING();
	}

	public BrainMethod(int mlocThreshold, float ccMlocThreshold, int maxNestingThreshold, int noavThreshold) {
		super();
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
	public void detect(AbstractTypeDeclaration type, AST ast, Document document) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			TypeDeclaration cls = (TypeDeclaration) type;

			methodsDoc = new ArrayList<Document>();

			for (MethodDeclaration method : cls.getMethods()) {
				boolean brainMethod = detect(type, method, ast);
				methodsDoc.add(new Document("method", method.getName()).append("value", brainMethod));
			}

			document.append("name", CodeSmellId.BRAIN_METHOD.toString()).append("methods", methodsDoc);
		}
	}

	public boolean detect(AbstractTypeDeclaration type, MethodDeclaration method, AST ast) {
		int cc = ccMetric.calculate(method);
		int mloc = mlocMetric.calculate(method, ast);
		int noav = noavMetric.calculate(type, method);
		int maxNesting = maxNestingMetric.calculate(method);

		return mloc > (mlocThreshold / 2) && cc >= ccThreshold && maxNesting >= maxNestingThreshold
				&& noav > noavThreshold;
	}

}