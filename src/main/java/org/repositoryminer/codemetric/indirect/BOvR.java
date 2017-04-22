package org.repositoryminer.codemetric.indirect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.ClassArchetype;
import org.repositoryminer.ast.ClassDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;

public class BOvR implements IIndirectCodeMetric {

	// The class name is the key and the list of its methods signature is the
	// value
	private Map<String, List<String>> methodsSignatures = new HashMap<String, List<String>>();

	// The class name is the key and the superclass is the value
	private Map<String, String> kinships = new HashMap<String, String>();

	// Stores the bovr value of each class
	private Map<String, Float> bovr = new HashMap<String, Float>();

	@Override
	public void calculate(AbstractClassDeclaration type, AST ast) {
		if (!type.getArchetype().equals(ClassArchetype.CLASS_OR_INTERFACE)) {
			return;
		}

		ClassDeclaration cls = (ClassDeclaration) type;

		List<String> methodsList = new ArrayList<String>();
		for (MethodDeclaration method : cls.getMethods()) {
			if (!method.isConstructor()) {
				methodsList.add(method.getName());
			}
		}
		methodsSignatures.put(cls.getName(), methodsList);

		String parentName = cls.getSuperClass() != null ? cls.getSuperClass().getName() : null;
		kinships.put(cls.getName(), parentName);
	}

	@Override
	public CodeMetricId getId() {
		return CodeMetricId.BOvR;
	}

	@Override
	public Map<String, Document> getResult() {
		calculateBOvR();

		Map<String, Document> result = new HashMap<String, Document>();
		for (Entry<String, Float> entry : bovr.entrySet()) {
			result.put(entry.getKey(),
					new Document("metric", CodeMetricId.BOvR.toString()).append("value", entry.getValue()));
		}

		clean();
		return result;
	}

	public void clean() {
		methodsSignatures.clear();
		kinships.clear();
		bovr.clear();
	}

	public Map<String, Float> getBOvR() {
		return bovr;
	}

	public void calculateBOvR() {
		for (Entry<String, String> kinship : kinships.entrySet()) {
			if (kinship.getValue() == null || methodsSignatures.get(kinship.getValue()) == null) {
				// No parent or is not possible find the parent
				bovr.put(kinship.getKey(), -1f);
				continue;
			}

			List<String> parentMethods = methodsSignatures.get(kinship.getValue());

			if (parentMethods.size() == 0) {
				bovr.put(kinship.getKey(), 0f);
				continue;
			}

			List<String> childMethods = methodsSignatures.get(kinship.getKey());

			int value = 0;

			for (String method : parentMethods) {
				if (childMethods.contains(method)) {
					value++;
				}
			}

			bovr.put(kinship.getKey(), value * 1.0f / parentMethods.size());
		}
	}

}