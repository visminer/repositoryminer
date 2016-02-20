package br.edu.ufba.softvis.visminer.metric;

import java.util.List;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.ast.TypeDeclaration;

@MetricAnnotation(
		name = "Weighted Method Count",
		description = "Weighted Method Count is the sum of the statical complexity of all methods of a class." 
				+ " The Cyclomatic complexity metric is used to quantify the method’s complexity.",
				acronym = "WMC")
public class WMCMetric extends MethodBasedMetricTemplate{

	@Override
	public void calculate(TypeDeclaration type, List<MethodDeclaration> methods, AST ast, Document document) {
		int wmc = calculate(methods);

		document.append("WMC", new Document("accumulated", new Integer(wmc)));
	}
	
	public int calculate(List<MethodDeclaration> methods){
		int wmc = 0;
		CCMetric cc = new CCMetric(); 

		for(MethodDeclaration method : methods){
			wmc += cc.calculate(method);	
		}
		
		return wmc;
	}

}