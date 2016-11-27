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
 * The sum of the complexity of all methods of a class. The CYCLO
 * metric is used to quantify the method’s complexity.
 */
public class WMC extends MethodBasedMetricTemplate {

	private CYCLO cc;
	
	public WMC() {
		cc = new CYCLO();
	}
	
	@Override
	public MetricId getId() {
		return MetricId.WMC;
	}
	
	@Override
	public Document calculate(AbstractTypeDeclaration type, List<MethodDeclaration> methods, AST ast) {
		int wmc = calculate(methods);
		return new Document("name", MetricId.WMC.toString()).append("value", new Integer(wmc));
	}

	public int calculate(List<MethodDeclaration> methods) {
		int wmc = 0;

		for (MethodDeclaration method : methods) {
			wmc += cc.calculate(method);
		}

		return wmc;
	}

}