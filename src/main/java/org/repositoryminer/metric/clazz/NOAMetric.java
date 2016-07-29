package org.repositoryminer.metric.clazz;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.ast.TypeDeclaration;

public class NOAMetric implements IClassMetric {

	@Override
	public void calculate(AbstractTypeDeclaration type, AST ast,
			Document document) {
		if (Archetype.CLASS_OR_INTERFACE == type.getArchetype()) {
			TypeDeclaration cls = (TypeDeclaration) type;
			document.append("name", NOA).append("accumulated", new Integer(calculate(cls.getFields())));
		}
	}

	public int calculate(List<FieldDeclaration> fields) {
		return (fields != null) ? fields.size() : 0;
	}

}
