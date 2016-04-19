package org.repositoryminer.antipattern;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.ClassOrInterfaceDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.SoftwareUnitType;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.metric.CCMetric;

public class ComplexMethod implements IAntiPattern {
	
	private List<Document> methodsDoc;
	private int ccThreshold = 4;
	
	public ComplexMethod() {}
	public ComplexMethod(int ccThreshold) {
		this.ccThreshold = ccThreshold;
	}

	@Override
	public void detect(TypeDeclaration type, AST ast, Document document) {
		if (type.getType() == SoftwareUnitType.CLASS_OR_INTERFACE) {
			ClassOrInterfaceDeclaration cls = (ClassOrInterfaceDeclaration) type;
			
			methodsDoc = new ArrayList<Document>();

			for(MethodDeclaration method : cls.getMethods()){
				boolean complexMethod = detect(method);
				methodsDoc.add(new Document("method", method.getName()).append("value", new Boolean(complexMethod)));
			}

			document.append("name", new String("Complex Method")).append("methods", methodsDoc);
		}
	}
	
	public boolean detect(MethodDeclaration method){
		boolean complexMethod = false;
		
		CCMetric ccMetric = new CCMetric();
		
		complexMethod = ccMetric.calculate(method) > ccThreshold;
		
		return complexMethod;
	}

}
