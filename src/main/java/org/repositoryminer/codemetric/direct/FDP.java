package org.repositoryminer.codemetric.direct;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.ast.Statement.NodeType;
import org.repositoryminer.codemetric.CodeMetricId;

/**
 * <h1>Foreign Data Provider</h1>
 * <p>
 * FDP is defined as the number of classes in which the attributes accessed - in conformity 
 * with the ATFD metric - are defined.
 */
public class FDP implements IDirectCodeMetric {

	@Override
	public CodeMetricId getId() {
		return CodeMetricId.FDP;
	}


	@Override
	public Document calculate(AbstractClassDeclaration type, AST ast) {
		Document cycloDocs = new Document("name", getId().toString());
		
		cycloDocs.append("methods", calculate(type)); 
		
		return cycloDocs;
	}

	public List<Document> calculate(AbstractClassDeclaration type) {
		
		List<Document> fdpDocs = new ArrayList<Document>();
		
		for (MethodDeclaration methodDeclaration : type.getMethods()) {
			float fdp = calculate(type, methodDeclaration);
			fdpDocs.add(new Document("method", methodDeclaration.getName()).append("value", fdp));
		}
		
		return fdpDocs;
	}

	public int calculate(AbstractClassDeclaration currType,MethodDeclaration methodDeclaration) {
		Set<String> accessedClass = new HashSet<String>();

		for (Statement stmt : methodDeclaration.getStatements()) {
			String exp, type;

			if (stmt.getNodeType() == NodeType.FIELD_ACCESS || stmt.getNodeType() == NodeType.METHOD_INVOCATION) {
				exp = stmt.getExpression();
				if( stmt.getNodeType().equals(NodeType.METHOD_INVOCATION)){
					exp = exp.substring(0, exp.indexOf("("));
				}
				type = exp.substring(0, exp.lastIndexOf("."));
			} else {
				continue;
			}

			if (stmt.getNodeType().equals(NodeType.FIELD_ACCESS)) {
				if (!currType.getName().equals(type))
					accessedClass.add(type.toLowerCase());
			} else if (stmt.getNodeType().equals(NodeType.METHOD_INVOCATION)) {
				String methodInv = exp.substring(exp.lastIndexOf(".") + 1);
				if (!currType.getName().equals(type)) {
					if ((methodInv.startsWith("get") || methodInv.startsWith("set")) && methodInv.length() > 3) {
						accessedClass.add(type.toLowerCase());
					} else if (methodInv.startsWith("is") && methodInv.length() > 2)
						accessedClass.add(type.toLowerCase());
				}
			}
		}

		return accessedClass.size();
		
	}
	


}
