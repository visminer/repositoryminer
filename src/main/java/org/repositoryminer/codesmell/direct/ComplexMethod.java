package org.repositoryminer.codesmell.direct;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.ClassArchetype;
import org.repositoryminer.ast.ClassDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;
import org.repositoryminer.codemetric.direct.CYCLO;
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

	private CYCLO ccMetric = new CYCLO();

	private int ccThreshold = 10;

	public ComplexMethod() {
	}

	public ComplexMethod(int ccThreshold) {
		this.ccThreshold = ccThreshold;
	}

	@Override
	public CodeSmellId getId() {
		return CodeSmellId.COMPLEX_METHOD;
	}

	@Override
	public Document detect(AbstractClassDeclaration type, AST ast) {
		if (type.getArchetype() == ClassArchetype.CLASS_OR_INTERFACE) {
			ClassDeclaration cls = (ClassDeclaration) type;
			List<Document> methods = new ArrayList<Document>();

			for (MethodDeclaration method : cls.getMethods()) {
				int cc = ccMetric.calculate(method);
				if (cc > ccThreshold) {
					Document mDoc = new Document("signature", method.getName()).append("metrics",
							new Document(CodeMetricId.CYCLO.toString(), cc));
					methods.add(mDoc);
				}
			}

			if (methods.size() > 0) {
				return new Document("codesmell", CodeSmellId.COMPLEX_METHOD.toString()).append("methods", methods);
			}
		}
		return null;
	}

	@Override
	public Document getThresholds() {
		return new Document("codesmell", CodeSmellId.COMPLEX_METHOD.toString()).append("thresholds",
				new Document(CodeMetricId.CYCLO.toString(), ccThreshold));
	}

}