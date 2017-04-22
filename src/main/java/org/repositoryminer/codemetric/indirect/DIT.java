package org.repositoryminer.codemetric.indirect;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.ClassArchetype;
import org.repositoryminer.ast.ClassDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;

public class DIT implements IIndirectCodeMetric {

	// DAG - Directed Acyclic Graph
	// The superclass name is used as key and class name as value
	// The classes relationship are structured as a DAG to further calculate DIT
	// using breadth-first search. A empty string is used as root node.
	// A child just has one father, so a simplified version of bfs is used.
	private Map<String, List<String>> dag = new HashMap<String, List<String>>();

	// Stores the DIT of each class.
	private Map<String, Integer> dit = new HashMap<String, Integer>();

	private Set<String> foundClasses = new HashSet<String>();

	@Override
	public void calculate(AbstractClassDeclaration type, AST ast) {
		if (!type.getArchetype().equals(ClassArchetype.CLASS_OR_INTERFACE)) {
			return;
		}

		ClassDeclaration cls = (ClassDeclaration) type;
		String clsName = cls.getName();
		String superClsName = cls.getSuperClass() == null ? "" : cls.getSuperClass().getName();

		foundClasses.add(clsName);

		if (dag.containsKey(superClsName)) {
			dag.get(superClsName).add(clsName);
		} else {
			List<String> auxList = new ArrayList<String>();
			auxList.add(clsName);
			dag.put(superClsName, auxList);
		}
	}

	@Override
	public CodeMetricId getId() {
		return CodeMetricId.DIT;
	}

	@Override
	public Map<String, Document> getResult() {
		calculateDIT();

		Map<String, Document> result = new HashMap<String, Document>();

		for (Entry<String, Integer> entry : dit.entrySet()) {
			result.put(entry.getKey(),
					new Document("metric", CodeMetricId.DIT.toString()).append("value", entry.getValue()));
		}

		clean();
		return result;
	}

	// calculates the DIT of the classes using breadth-first search
	public void calculateDIT() {
		Queue<String> queue = new ArrayDeque<String>();
		queue.add(""); // "" is the root

		dit.put("", -1);

		for (String superClass : dag.keySet()) {
			if (!foundClasses.contains(superClass)) {
				// Add as roots super classes that the source code is unavailable
				queue.add(superClass);
				dit.put(superClass, -1);
			}
		}

		while (!queue.isEmpty()) {
			String s = queue.poll();

			List<String> adjs = dag.get(s);

			// We reach a leaf node
			if (adjs == null) {
				continue;
			}

			for (String w : adjs) {
				queue.add(w);
				dit.put(w, dit.get(s) + 1);
			}
		}

		dit.remove(""); // removing the fake root from the result

		// removes the superclass that the source was not found
		for (String superClass : dag.keySet()) {
			if (!foundClasses.contains(superClass)) {
				dit.remove(superClass);
			}
		}
	}

	// Do not forget to clean your mess after finish the job, another checkout
	// can use this class.
	public void clean() {
		dag.clear();
		dit.clear();
		foundClasses.clear();
	}

	public Map<String, Integer> getDIT() {
		return dit;
	}

}