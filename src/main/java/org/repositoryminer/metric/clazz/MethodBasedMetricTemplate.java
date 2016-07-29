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

public abstract class MethodBasedMetricTemplate implements IClassMetric {

	protected List<FieldDeclaration> currentFields = new ArrayList<FieldDeclaration>();

	@Override
	public void calculate(AbstractTypeDeclaration type, AST ast,
			Document document) {
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

	public abstract void calculate(AbstractTypeDeclaration type,
			List<MethodDeclaration> methods, AST ast, Document document);

}
