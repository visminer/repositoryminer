package br.edu.ufba.softvis.visminer.metric;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.ast.Statement;
import br.edu.ufba.softvis.visminer.ast.TypeDeclaration;
import br.edu.ufba.softvis.visminer.constant.NodeType;

@MetricAnnotation(name = "Access To Foreign Data", description = "Access To Foreign Data (ATFD) counts the number of attributes"
		+ "from unrelated classes that are accessed directly or by invoking accessor methods.", 
		acronym = "ATFD")
public class ATFDMetric extends MethodBasedMetricTemplate {

	private List<Document> methodsDoc;
	
	@Override
	public void calculate(TypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		methodsDoc = new ArrayList<Document>();

		int atfdClass = calculate(type, methods, true);
		document.append("ATFD", new Document("accumulated", new Integer(atfdClass)).append("methods", methodsDoc));
	}
	
	public int calculate(TypeDeclaration type, List<MethodDeclaration> methods, boolean calculateByMethod){
		int atfdClass = 0;

		for (MethodDeclaration mDeclaration : methods) {
			int atfdMethod = countForeignAccessedFields(type, mDeclaration);
			
			atfdClass += atfdMethod;
			if (calculateByMethod) 
			{
				methodsDoc.add(new Document("method", mDeclaration.getName()).append("value", new Integer(atfdMethod)));
			}
		}
		
		return atfdClass;
	}

	private int countForeignAccessedFields(TypeDeclaration currType, MethodDeclaration method) {
		Set<String> accessedFields = new HashSet<String>();

		for (Statement stm : method.getStatements()) {
			if (stm.getNodeType().equals(NodeType.FIELD_ACCESS)) {
				String exp = stm.getExpression();
				String type = exp.substring(0, exp.lastIndexOf("."));
				if (!currType.getName().equals(type))
					accessedFields.add(exp.toLowerCase());
			} else if (stm.getNodeType().equals(NodeType.METHOD_INVOCATION)) {
				String exp = stm.getExpression();
				String type = exp.substring(0, exp.lastIndexOf("."));
				String methodInv = exp.substring(exp.lastIndexOf(".") + 1);
				if (!currType.getName().equals(type)) {
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
