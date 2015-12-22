package br.edu.ufba.softvis.visminer.metric;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.ast.Statement;
import br.edu.ufba.softvis.visminer.constant.NodeType;

@MetricAnnotation(name = "Access To Foreign Data", description = "Access To Foreign Data (ATFD) counts the number of attributes"
		+ "from unrelated classes that are accessed directly or by invoking accessor methods.", 
		acronym = "ATFD")
public class ATFDMetric extends MethodBasedMetricTemplate {

	@Override
	public void calculate(List<MethodDeclaration> methods, Document document) {
		List<Document> methodsDoc = new ArrayList<Document>();

		int atfdClass = 0;
		for (MethodDeclaration mDeclaration : methods) {
			int atfdMethod = countForeignAccessedFields(mDeclaration);
			atfdClass += atfdMethod;
			methodsDoc.add(new Document("method", mDeclaration.getName()).append("value", atfdMethod + ""));
		}

		document.append("ATFD", new Document("accumulated", atfdClass + "").append("methods", methodsDoc));
	}

	private int countForeignAccessedFields(MethodDeclaration method) {
		Set<String> accessedFields = new HashSet<String>();

		for (Statement stm : method.getStatements()) {
			if (stm.getNodeType().equals(NodeType.FIELD_ACCESS)) {
				String exp = stm.getExpression();
				String type = exp.substring(0, exp.lastIndexOf("."));
				if (!currentType.getName().equals(type))
					accessedFields.add(exp.toLowerCase());
			} else if (stm.getNodeType().equals(NodeType.METHOD_INVOCATION)) {
				String exp = stm.getExpression();
				String type = exp.substring(0, exp.lastIndexOf("."));
				String methodInv = exp.substring(exp.lastIndexOf(".") + 1);
				if (!currentType.getName().equals(type)) {
					if ((methodInv.startsWith("get") || methodInv.startsWith("set")) && methodInv.length() > 3) {
						accessedFields.add((type + "." + methodInv.substring(3)).toLowerCase());
					} else if (methodInv.startsWith("is") && methodInv.length() > 2)
						accessedFields.add((type + "." + methodInv.substring(2)).toLowerCase());
				}
			}

		}

		return accessedFields.size();
	}

}
