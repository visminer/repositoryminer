package org.repositoryminer.metric.clazz;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.metric.MetricId;

/**
 * <h1>Number of attributes</h1>
 * <p>
 * NOA is defined as the number of attributes in a class.
 */
public class NOA implements IClassMetric {

	@Override
	public void calculate(AbstractTypeDeclaration type, AST ast, Document document) {
		if (Archetype.CLASS_OR_INTERFACE == type.getArchetype()) {
			TypeDeclaration cls = (TypeDeclaration) type;
			document.append("name", MetricId.NOA).append("accumulated", new Integer(calculate(cls.getFields())));
		}
	}

	public int calculate(List<FieldDeclaration> fields) {
		return (fields != null) ? fields.size() : 0;
	}

}