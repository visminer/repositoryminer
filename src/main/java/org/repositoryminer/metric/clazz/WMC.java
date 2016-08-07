package org.repositoryminer.metric.clazz;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.metric.MetricId;

/**
 * <h1>Weighted Method Count</h1>
 * <p>
 * The sum of the statical complexity of all methods of a class. The CYCLO
 * metric is used to quantify the method’s complexity.
 */
public class WMC extends MethodBasedMetricTemplate {

	@Override
	public void calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		int wmc = calculate(methods);
		document.append("name", MetricId.WMC).append("accumulated", new Integer(wmc));
	}

	public int calculate(List<MethodDeclaration> methods) {
		int wmc = 0;
		CYCLO cc = new CYCLO();

		for (MethodDeclaration method : methods) {
			wmc += cc.calculate(method);
		}

		return wmc;
	}

}