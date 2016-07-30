package org.repositoryminer.metric.clazz;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;

/**
 * <h1>A method-oriented version of metric</h1>
 * <p>
 * We have divided the metrics into two distinct sets:
 * <ul>
 * <li>Class-oriented metrics -> represent metrics that pertain to the whole
 * class. Such metrics are likely to be inherited from
 * {@link org.repositoryminer.metric.clazz.IClassMetric}
 * <li>Method-oriented metrics -> metrics extracted from methods contained in
 * the class
 * </ul>
 * <p>
 * Given an abstract type that represents a class or an interface, all its
 * methods can/must be examined to extract metrics values from method's body of
 * code.
 * <p>
 * The actual calculation of the metric is delegated to sub-types that must
 * implement {@link #calculate(AbstractTypeDeclaration, List, AST, Document)}.
 * <p>
 * It is up to the implementations of this interface to properly decide how to
 * persist the data extracted from a given AST (
 * {@link org.repositoryminer.ast.AST}). After metrics' values are obtained they
 * can be pushed to the instance of org.bson.Document injected into
 * {@link #calculate(AbstractTypeDeclaration, AST, Document)}.
 */
public abstract class MethodBasedMetricTemplate implements IClassMetric {

	protected List<FieldDeclaration> currentFields = new ArrayList<FieldDeclaration>();

	/**
	 * @see IClassMetric#calculate(AbstractTypeDeclaration, AST, Document)
	 */
	@Override
	public void calculate(AbstractTypeDeclaration type, AST ast, Document document) {
		TypeDeclaration cls = null;
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			cls = (TypeDeclaration) type;

			if (cls.getMethods() != null) {
				if (cls.getFields() != null) {
					currentFields = cls.getFields();
				}
				calculate(type, cls.getMethods(), ast, document);
			}
		}
	}

	/**
	 * Delegates the calculation of metrics to sub-classes
	 * 
	 * Any class inherited from this one must define the adequate routine to
	 * obtain the metrics' values from the body of all methods represented as
	 * instances of {@link org.repositoryminer.ast.MethodDeclaration}
	 * <p>
	 * The provided instance org.bson.Document can be used at will to better
	 * suit the representation of the metrics.
	 * 
	 * @param type
	 *            the original type from which the list of methods were obtained
	 * @param methods
	 *            the list of methods obtained from the abstracted type
	 * @param ast
	 *            the AST obtained from the type
	 * @param document
	 *            root element of a mongodb document from which all
	 *            metrics-values pairs must be arranged
	 */
	public abstract void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast,
			Document document);

}
