package org.repositoryminer.metric;

import java.util.List;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.listener.IMetricCalculationListener;

public class NOAMetric implements ICommitMetric {

	@Override
	public void calculate(AbstractTypeDeclaration type, AST ast, IMetricCalculationListener listener) {
		if (Archetype.CLASS_OR_INTERFACE == type.getArchetype()) {
			TypeDeclaration cls = (TypeDeclaration) type;
			listener.updateMetricValue(NOA, calculate(cls.getFields()));
		}
	}

	public int calculate(List<FieldDeclaration> fields) {
		return (fields != null) ? fields.size() : 0;
	}

}
