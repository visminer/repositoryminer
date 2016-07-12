package org.repositoryminer.codesmell.commit;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.codesmell.CodeSmellId;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.metric.CCMetric;

public class ComplexMethod implements ICommitCodeSmell {
	
	private List<Document> methodsDoc;
	private int ccThreshold = 4;
	
	public ComplexMethod() {}
	
	public ComplexMethod(int ccThreshold) {
		this.ccThreshold = ccThreshold;
	}

	@Override
	public void detect(AbstractTypeDeclaration type, AST ast, Document document) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			TypeDeclaration cls = (TypeDeclaration) type;
			
			methodsDoc = new ArrayList<Document>();

			for(MethodDeclaration method : cls.getMethods()){
				boolean complexMethod = detect(method);
				methodsDoc.add(new Document("method", method.getName()).append("value", new Boolean(complexMethod)));
			}

			document.append("name", CodeSmellId.COMPLEX_METHOD).append("methods", methodsDoc);
		}
	}
	
	public boolean detect(MethodDeclaration method){
		boolean complexMethod = false;
		
		CCMetric ccMetric = new CCMetric();
		
		complexMethod = ccMetric.calculate(method) > ccThreshold;
		
		return complexMethod;
	}

}
