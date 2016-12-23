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
import org.repositoryminer.metric.MetricId;
import org.repositoryminer.metric.clazz.MLOC;

public class LongMethod implements IClassCodeSmell {

	private List<Document> methodsDoc;
	private MLOC mlocMetric;
	
	private int mlocThreshold = 40;
	
	public LongMethod() {
		mlocMetric = new MLOC();
	}
	
	public LongMethod(int mlocThreshold) {
		this();
		this.mlocThreshold = mlocThreshold;
	}

	@Override
	public CodeSmellId getId() {
		return CodeSmellId.LONG_METHOD;
	}
	
	@Override
	public Document detect(AbstractTypeDeclaration type, AST ast) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			TypeDeclaration cls = (TypeDeclaration) type;
			methodsDoc = new ArrayList<Document>();

			for(MethodDeclaration method : cls.getMethods()){
				int mlocValue = mlocMetric.calculate(method, ast);
				
				Document methodDoc = new Document("method", method.getName());
				methodDoc.append("metrics", new Document(MetricId.MLOC.toString(), mlocValue));
				methodDoc.append("is_smell", mlocValue > mlocThreshold);
				
				methodsDoc.add(methodDoc);
			}

			Document response = new Document("name", CodeSmellId.LONG_METHOD.toString());
			response.append("thresholds", new Document(MetricId.MLOC.toString(), mlocThreshold));
			response.append("methods", methodsDoc);
			
			return response;
		}
		return null;
	}
	
}