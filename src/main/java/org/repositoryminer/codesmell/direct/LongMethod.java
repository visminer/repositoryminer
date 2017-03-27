package org.repositoryminer.codesmell.direct;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;
import org.repositoryminer.codemetric.direct.MLOC;
import org.repositoryminer.codesmell.CodeSmellId;

public class LongMethod implements IDirectCodeSmell {

	private MLOC mlocMetric = new MLOC();

	private int mlocThreshold = 40;

	public LongMethod() {
	}

	public LongMethod(int mlocThreshold) {
		this.mlocThreshold = mlocThreshold;
	}

	@Override
	public CodeSmellId getId() {
		return CodeSmellId.LONG_METHOD;
	}

	@Override
	public Document detect(AbstractClassDeclaration type, AST ast) {
		List<Document> methods = new ArrayList<Document>();

		for (MethodDeclaration method : type.getMethods()) {
			int mloc = mlocMetric.calculate(method, ast);
			if (mloc > mlocThreshold) {
				Document mDoc = new Document("signature", method.getName()).append("metrics",
						new Document(CodeMetricId.MLOC.toString(), mloc));
				methods.add(mDoc);
			}
		}

		if (methods.size() > 0) {
			return new Document("codesmell", CodeSmellId.LONG_METHOD.toString()).append("methods", methods);
		}
		return null;
	}

	@Override
	public Document getThresholds() {
		return new Document("codesmell", CodeSmellId.LONG_METHOD.toString()).append("thresholds",
				new Document(CodeMetricId.MLOC.toString(), mlocThreshold));
	}

}