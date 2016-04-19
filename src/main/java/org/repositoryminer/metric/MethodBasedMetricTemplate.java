package org.repositoryminer.metric;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.ClassOrInterfaceDeclaration;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.SoftwareUnitType;
import org.repositoryminer.ast.TypeDeclaration;


public abstract class MethodBasedMetricTemplate implements IMetric {

	protected List<FieldDeclaration> currentFields = new ArrayList<FieldDeclaration>();

	@Override
	public void calculate(TypeDeclaration type, AST ast, Document document) {
		ClassOrInterfaceDeclaration cls = null;
		if (type.getType() == SoftwareUnitType.CLASS_OR_INTERFACE) {
			cls = (ClassOrInterfaceDeclaration) type;

			if (cls.getMethods() != null) {
				if (cls.getFields() != null)
				{
					currentFields = cls.getFields();
				}

				calculate(type, cls.getMethods(), ast, document);
			}
		}
	}

	public abstract void calculate(TypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document);

}
