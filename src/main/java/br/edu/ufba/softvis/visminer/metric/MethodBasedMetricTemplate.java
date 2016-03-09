package br.edu.ufba.softvis.visminer.metric;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.ClassOrInterfaceDeclaration;
import br.edu.ufba.softvis.visminer.ast.FieldDeclaration;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.ast.TypeDeclaration;
import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;

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
