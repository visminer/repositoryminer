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
import org.repositoryminer.metric.MetricId;
import org.repositoryminer.metric.clazz.CYCLO;

/**
 * <h1>Complex Method</h1>
 * <p>
 * This code smell is used to detect methods which are too complex. Complex
 * methods causes negative impact in software understandability and maintenance.
 * <p>
 * A method is considered too complex when it has McCabe’s cyclomatic number too
 * high. The default threshold for cyclomatic complexity is 10.
 */
public class ComplexMethod implements IClassCodeSmell {

	private CYCLO ccMetric;

	private int ccThreshold = 10;

	public ComplexMethod() {
		ccMetric = new CYCLO();
	}

	public ComplexMethod(int ccThreshold) {
		this();
		this.ccThreshold = ccThreshold;
	}

	@Override
	public CodeSmellId getId() {
		return CodeSmellId.COMPLEX_METHOD;
	}

	@Override
	public Document detect(AbstractTypeDeclaration type, AST ast) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			TypeDeclaration cls = (TypeDeclaration) type;
			List<String> methods = new ArrayList<String>();

			for (MethodDeclaration method : cls.getMethods()) {
				if (detect(method)) {
					methods.add(method.getName());
				}
			}

			if (methods.size() > 0) {
				return new Document("codesmell", CodeSmellId.COMPLEX_METHOD.toString()).append("methods", methods);
			}
		}
		return null;
	}

	public boolean detect(MethodDeclaration method) {
		return ccMetric.calculate(method) > ccThreshold;
	}

	@Override
	public Document getThresholds() {
		return new Document("codesmell", CodeSmellId.COMPLEX_METHOD.toString()).append("thresholds",
				new Document(MetricId.CYCLO.toString(), ccThreshold));
	}

}