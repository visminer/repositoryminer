package org.repositoryminer.metric;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.DeclarationType;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration;

public class NOAMetric implements IMetric {

	@Override
	public void calculate(AbstractTypeDeclaration type, AST ast,
			Document document) {
		if (DeclarationType.CLASS_OR_INTERFACE == type.getType()) {
			TypeDeclaration cls = (TypeDeclaration) type;
			document.append("name", NOA).append("accumulated", new Integer(calculate(cls.getFields())));
		}
	}

	public int calculate(List<FieldDeclaration> fields) {
		return (fields != null) ? fields.size() : 0;
	}

}
