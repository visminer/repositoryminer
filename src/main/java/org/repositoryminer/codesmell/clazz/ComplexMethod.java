package org.repositoryminer.codesmell.clazz;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.codesmell.CodeSmellId;
import org.repositoryminer.metric.clazz.CYCLO;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.TypeDeclaration;

/**
 * <h1>Complex Method</h1>
 * <p>
 * This code smell is used to detect methods which are too complex. Complex
 * methods causes negative impact in software understandability and maintenance.
 * <p>
 * A method is considered too complex when it has McCabe’s cyclomatic number too high.
 * The default threshold for cyclomatic complexity is 4.
 */
public class ComplexMethod implements IClassCodeSmell {

	private List<Document> methodsDoc;
	private int ccThreshold = 4;

	public ComplexMethod() {
	}

	public ComplexMethod(int ccThreshold) {
		this.ccThreshold = ccThreshold;
	}

	@Override
	public void detect(AbstractTypeDeclaration type, AST ast, Document document) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			TypeDeclaration cls = (TypeDeclaration) type;

			methodsDoc = new ArrayList<Document>();

			for (MethodDeclaration method : cls.getMethods()) {
				boolean complexMethod = detect(method);
				methodsDoc.add(new Document("method", method.getName()).append("value", new Boolean(complexMethod)));
			}

			document.append("name", CodeSmellId.COMPLEX_METHOD).append("methods", methodsDoc);
		}
	}

	public boolean detect(MethodDeclaration method) {
		boolean complexMethod = false;
		CYCLO ccMetric = new CYCLO();
		complexMethod = ccMetric.calculate(method) > ccThreshold;
		return complexMethod;
	}

}