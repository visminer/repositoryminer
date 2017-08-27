package org.repositoryminer.codemetric.indirect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.ClassArchetype;
import org.repositoryminer.ast.ClassDeclaration;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.ast.Statement.NodeType;
import org.repositoryminer.codemetric.CodeMetricId;
import org.repositoryminer.codemetric.direct.NProtM;

public class BUR implements IIndirectCodeMetric {

	// Stores the protected methods in the class
	// Key - the class name
	// Value - list of protected methods
	private Map<String, List<String>> protMethods = new HashMap<String, List<String>>();

	// Stores the protected fields in the class
	// Key - the class name
	// Value - list of protected fields
	private Map<String, List<String>> protFields = new HashMap<String, List<String>>();

	// Stores the parent fields accessed by child class methods
	// Key - the child class name
	// Value - parent fields accessed
	private Map<String, Set<String>> accessedFields = new HashMap<String, Set<String>>();

	// Stores the parent methods invoked by child class methods
	// Key - the child class name
	// Value - parent methods invoked
	private Map<String, Set<String>> invokedMethods = new HashMap<String, Set<String>>();

	// Stores the relationship between child and parent
	// Key - the child class name
	// Value - parent class name
	private Map<String, String> kinships = new HashMap<String, String>();

	// Stores the bur value
	// Key - The class
	// Value - BUR metric value
	private Map<String, Float> bur = new HashMap<String, Float>();

	private NProtM nprotm = new NProtM();
	
	@Override
	public void calculate(AbstractClassDeclaration type, AST ast) {
		if (!type.getArchetype().equals(ClassArchetype.CLASS_OR_INTERFACE)) {
			return;
		}

		ClassDeclaration cls = (ClassDeclaration) type;

		// find protected methods in the class
		List<String> methodsList = new ArrayList<String>();
		for (MethodDeclaration method : cls.getMethods()) {
			if (nprotm.isProtected(method.getModifiers())) {
				methodsList.add(method.getName());
			}
		}
		protMethods.put(cls.getName(), methodsList);

		// find protected fields in the class
		List<String> fieldsList = new ArrayList<String>();
		for (FieldDeclaration field : cls.getFields()) {
			if (nprotm.isProtected(field.getModifiers())) {
				fieldsList.add(field.getName());
			}
		}
		protFields.put(cls.getName(), fieldsList);

		String parentName = cls.getSuperClass() != null ? cls.getSuperClass().getName() : null;
		kinships.put(cls.getName(), parentName);

		// No parent, so nothing more to do here
		if (parentName == null) {
			return;
		}

		// If the class has a parent bur should be calculate
		invokedMethods.put(cls.getName(), new HashSet<String>());
		accessedFields.put(cls.getName(), new HashSet<String>());
		for (MethodDeclaration method : cls.getMethods()) {
			processAccessAndInvocations(cls.getName(), parentName, method);
		}
	}

	@Override
	public CodeMetricId getId() {
		return CodeMetricId.BUR;
	}

	@Override
	public Map<String, Document> getResult() {
		calculateBUR();

		Map<String, Document> result = new HashMap<String, Document>();
		for (Entry<String, Float> entry : bur.entrySet()) {
			result.put(entry.getKey(),
					new Document("metric", CodeMetricId.BUR.toString()).append("value", entry.getValue()));
		}

		clean();

		return result;
	}

	public void clean() {
		protFields.clear();
		protMethods.clear();
		accessedFields.clear();
		invokedMethods.clear();
		kinships.clear();
		bur.clear();
	}

	public Map<String, Float> getBUR() {
		return bur;
	}

	public void calculateBUR() {
		for (Entry<String, String> kinship : kinships.entrySet()) {
			if (kinship.getValue() == null || protMethods.get(kinship.getValue()) == null) {
				// No parent or is not possible find the parent
				bur.put(kinship.getKey(), -1f);
				continue;
			}

			List<String> parentFields = protFields.get(kinship.getValue());
			List<String> parentMethods = protMethods.get(kinship.getValue());

			int totalProtected = parentFields.size() + parentMethods.size();

			if (totalProtected == 0) {
				bur.put(kinship.getKey(), 0f);
				continue;
			}

			Set<String> childFields = accessedFields.get(kinship.getKey());
			Set<String> childMethods = invokedMethods.get(kinship.getKey());

			int childUses = 0;

			for (String parentField : parentFields) {
				if (childFields.contains(parentField)) {
					childUses++;
				}
			}

			for (String parentMethod : parentMethods) {
				if (childMethods.contains(parentMethod)) {
					childUses++;
				}
			}

			bur.put(kinship.getKey(), childUses * 1.0f / totalProtected);
		}
	}

	private void processAccessAndInvocations(String clazz, String parent, MethodDeclaration method) {
		for (Statement stmt : method.getStatements()) {
			String exp, type, target;

			if (stmt.getNodeType() == NodeType.FIELD_ACCESS || stmt.getNodeType() == NodeType.METHOD_INVOCATION) {
				exp = stmt.getExpression();
				type = exp.substring(0, exp.lastIndexOf("."));
				target = exp.substring(exp.lastIndexOf(".") + 1);
			} else {
				continue;
			}

			// The accessed field or invoked method belongs to the parent
			if (parent.equals(type)) {
				if (stmt.getNodeType() == NodeType.FIELD_ACCESS) {
					accessedFields.get(clazz).add(target);
				} else {
					invokedMethods.get(clazz).add(target);
				}
			}
		}
	}

}