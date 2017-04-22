package org.repositoryminer.codesmell.indirect;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;
import org.repositoryminer.codemetric.indirect.DIT;
import org.repositoryminer.codesmell.CodeSmellId;

public class DepthOfInheritanceTree implements IIndirectCodeSmell {

	private DIT ditMetric = new DIT();

	private int ditThreshold = 2;

	public DepthOfInheritanceTree() {
	}

	public DepthOfInheritanceTree(int ditThreshold) {
		this.ditThreshold = ditThreshold;
	}

	@Override
	public void detect(AbstractClassDeclaration type, AST ast) {
		ditMetric.calculate(type, ast);
	}

	@Override
	public CodeSmellId getId() {
		return CodeSmellId.DEPTH_OF_INHERITANCE_TREE;
	}

	@Override
	public Document getThresholds() {
		return new Document("codesmell", CodeSmellId.DEPTH_OF_INHERITANCE_TREE.toString()).append("thresholds",
				new Document(CodeMetricId.DIT.toString(), ditThreshold));
	}

	@Override
	public Map<String, Document> getResult() {
		ditMetric.calculateDIT();

		Map<String, Document> result = new HashMap<String, Document>();

		for (Entry<String, Integer> entry : ditMetric.getDIT().entrySet()) {
			if (entry.getValue() > ditThreshold) {
				result.put(entry.getKey(), new Document("codesmell", CodeSmellId.DEPTH_OF_INHERITANCE_TREE.toString())
						.append("metrics", new Document(CodeMetricId.DIT.toString(), entry.getKey())));
			}
		}

		ditMetric.clean();
		return result;
	}

}