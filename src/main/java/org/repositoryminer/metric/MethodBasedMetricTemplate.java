package org.repositoryminer.metric;

import java.util.ArrayList;
import java.util.List;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.listener.IMetricCalculationListener;

public abstract class MethodBasedMetricTemplate implements ICommitMetric {

	protected List<FieldDeclaration> currentFields = new ArrayList<FieldDeclaration>();

	@Override
	public void calculate(AbstractTypeDeclaration type, AST ast, IMetricCalculationListener listener) {
		TypeDeclaration cls = null;
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			cls = (TypeDeclaration) type;

			if (cls.getMethods() != null) {
				if (cls.getFields() != null) {
					currentFields = cls.getFields();
				}

				calculate(type, cls.getMethods(), ast, listener);
			}
		}
	}

	public abstract void calculate(AbstractTypeDeclaration type,
			List<MethodDeclaration> methods, AST ast, IMetricCalculationListener listener);

}
