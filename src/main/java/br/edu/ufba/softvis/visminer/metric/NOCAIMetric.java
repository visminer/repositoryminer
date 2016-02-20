package br.edu.ufba.softvis.visminer.metric;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;

@MetricAnnotation(
	name = "Number of Classes and Interfaces", 
	description = "Number of Classes and Interfaces is a software metric used to measure the size of a "
			+ "computer program"
		+ " by counting the concrete and abstract classes", 
	acronym = "NOCAI")
public class NOCAIMetric implements IMetric {

	@Override
	public void calculate(AST ast, Document document) {

		Map<String, Integer> packageCls = new HashMap<String, Integer>();
		int projectQtd = 0;
		for (AST ast : astList) {

			Document doc = ast.getDocument();
			int id, num = 0;

			if (doc.getTypes() != null) {
				num += doc.getTypes().size();
			}

			if (doc.getPackageDeclaration() != null) {

				id = doc.getPackageDeclaration().getId();

				if (packageCls.containsKey(id)) {
					int aux = packageCls.get(id);
					packageCls.put(id + "", aux + num);
				} else {
					packageCls.put(id + "", num);
				}

			}

			projectQtd += num;

		}

		for (Entry<String, Integer> entry : packageCls.entrySet()) {
			persistence.postMetricValue(entry.getKey(), String.valueOf(entry.getValue()));
		}

		persistence.postMetricValue(persistence.getProject().getUid(), String.valueOf(projectQtd));
	}

}