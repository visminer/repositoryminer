package org.repositoryminer.codesmell.clazz;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.codesmell.CodeSmellId;
import org.repositoryminer.metric.clazz.MLOC;

public class LongMethod implements IClassCodeSmell {

	private List<Document> methodsDoc;
	private MLOC mlocMetric;
	
	private int mlocThreshold = 40;
	
	public LongMethod() {
		mlocMetric = new MLOC();
	}
	
	public LongMethod(int mlocThreshold) {
		super();
		this.mlocThreshold = mlocThreshold;
	}

	@Override
	public CodeSmellId getId() {
		return CodeSmellId.LONG_METHOD;
	}
	
	@Override
	public void detect(AbstractTypeDeclaration type, AST ast, Document document) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			TypeDeclaration cls = (TypeDeclaration) type;
			methodsDoc = new ArrayList<Document>();

			for(MethodDeclaration method : cls.getMethods()){
				methodsDoc.add(new Document("method", method.getName()).append("value", detect(method, ast)));
			}

			document.append("name", CodeSmellId.LONG_METHOD.toString()).append("methods", methodsDoc);
		}
	}
	
	public boolean detect(MethodDeclaration method, AST ast){
		return mlocMetric.calculate(method, ast) > mlocThreshold;
	}

}