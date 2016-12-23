package org.repositoryminer.codesmell.clazz;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.codesmell.CodeSmellId;
import org.repositoryminer.metric.MetricId;
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
 * The default threshold for cyclomatic complexity is 10.
 */
public class ComplexMethod implements IClassCodeSmell {

	private List<Document> methodsDoc;
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

			methodsDoc = new ArrayList<Document>();

			for (MethodDeclaration method : cls.getMethods()) {
				int ccValue = ccMetric.calculate(method);
				
				Document methodDoc = new Document("method", method.getName());
				methodDoc.append("metrics", new Document(MetricId.CYCLO.toString(), ccValue));
				methodDoc.append("is_smell", ccValue > ccThreshold);
				
				methodsDoc.add(methodDoc);
			}

			Document response = new Document("name", CodeSmellId.COMPLEX_METHOD.toString());
			response.append("thresholds", new Document(MetricId.CYCLO.toString(), ccThreshold));
			response.append("methods", methodsDoc);
		}
		return null;
	}

}