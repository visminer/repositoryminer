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
			List<String> methods = new ArrayList<String>();

			for(MethodDeclaration method : cls.getMethods()){
				if (detect(method, ast)) {
					methods.add(method.getName());
				}
			}

			if (methods.size() > 0) {
				return new Document("name", CodeSmellId.LONG_METHOD.toString()).append("methods", methods);
			}
		}
		return null;
	}

	public boolean detect(MethodDeclaration method, AST ast){
		return mlocMetric.calculate(method, ast) > mlocThreshold;
	}

	@Override
	public Document getThresholds() {
		return new Document(MetricId.MLOC.toString(), mlocThreshold);
	}

}