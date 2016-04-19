package org.repositoryminer.metric;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.ClassOrInterfaceDeclaration;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.ast.SoftwareUnitType;
import org.repositoryminer.ast.TypeDeclaration;

public class NOAMetric implements IMetric {

	@Override
	public void calculate(TypeDeclaration type, AST ast, Document document) {
		if (SoftwareUnitType.CLASS_OR_INTERFACE.equals(type.getType())) {
			ClassOrInterfaceDeclaration cls = (ClassOrInterfaceDeclaration) type;
			document.append("NOA", new Document("accumulated", new Integer(calculate(cls.getFields()))));
		}
	}

	public int calculate(List<FieldDeclaration> fields) {
		int noa = 0;

		if (fields != null) {
			noa += fields.size();
		}

		return noa;
	}

}
