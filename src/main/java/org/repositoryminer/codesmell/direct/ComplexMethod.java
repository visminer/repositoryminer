package org.repositoryminer.codesmell.direct;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.AbstractClassDeclaration.Archetype;
import org.repositoryminer.codemetric.CodeMetricId;
import org.repositoryminer.codemetric.direct.CYCLO;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.ClassDeclaration;
import org.repositoryminer.codesmell.CodeSmellId;

/**
 * <h1>Complex Method</h1>
 * <p>
 * This code smell is used to detect methods which are too complex. Complex
 * methods causes negative impact in software understandability and maintenance.
 * <p>
 * A method is considered too complex when it has McCabe’s cyclomatic number too
 * high. The default threshold for cyclomatic complexity is 10.
 */
public class ComplexMethod implements IDirectCodeSmell {

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
	public Document detect(AbstractClassDeclaration type, AST ast) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			ClassDeclaration cls = (ClassDeclaration) type;
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
				new Document(CodeMetricId.CYCLO.toString(), ccThreshold));
	}

}