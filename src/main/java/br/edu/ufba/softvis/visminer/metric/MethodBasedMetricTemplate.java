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

	protected TypeDeclaration currentType;
	protected List<FieldDeclaration> currentFields = new ArrayList<FieldDeclaration>();

	@Override
	public void calculate(AST ast, Document document) {
		if (ast.getDocument().getTypes() != null) {
			for (TypeDeclaration type : ast.getDocument().getTypes()) {
				ClassOrInterfaceDeclaration cls = null;
				if (type.getType() == SoftwareUnitType.CLASS_OR_INTERFACE) {
					cls = (ClassOrInterfaceDeclaration) type;
				} else {
					continue;
				}

				if (cls.getMethods() == null) {
					continue;
				}

				if (cls.getFields() != null)
					currentFields = cls.getFields();

				currentType = type;

				calculate(cls.getMethods(), document);
			}
		}
	}

	public abstract void calculate(List<MethodDeclaration> methods, Document document);

}
