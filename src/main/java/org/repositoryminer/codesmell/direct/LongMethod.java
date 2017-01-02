package org.repositoryminer.codesmell.direct;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.AbstractClassDeclaration.Archetype;
import org.repositoryminer.codemetric.CodeMetricId;
import org.repositoryminer.codemetric.direct.MLOC;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.ClassDeclaration;
import org.repositoryminer.codesmell.CodeSmellId;

public class LongMethod implements IDirectCodeSmell {

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
	public Document detect(AbstractClassDeclaration type, AST ast) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			ClassDeclaration cls = (ClassDeclaration) type;
			List<String> methods = new ArrayList<String>();

			for (MethodDeclaration method : cls.getMethods()) {
				if (detect(method, ast)) {
					methods.add(method.getName());
				}
			}

			if (methods.size() > 0) {
				return new Document("codesmell", CodeSmellId.LONG_METHOD.toString()).append("methods", methods);
			}
		}
		return null;
	}

	public boolean detect(MethodDeclaration method, AST ast) {
		return mlocMetric.calculate(method, ast) > mlocThreshold;
	}

	@Override
	public Document getThresholds() {
		return new Document("codesmell", CodeSmellId.LONG_METHOD.toString()).append("thresholds",
				new Document(CodeMetricId.MLOC.toString(), mlocThreshold));
	}

}