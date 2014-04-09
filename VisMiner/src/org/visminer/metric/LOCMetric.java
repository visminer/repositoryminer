package org.visminer.metric;

import org.eclipse.jface.text.Document;
import org.visminer.constants.Metrics;
import org.visminer.util.DetailAST;

public class LOCMetric implements IMetric{

	public Metrics getId(){
		return Metrics.LOC;
	}
	
	public int calculate(DetailAST ast){
		
		Document doc = new Document(ast.getSource());
		return doc.getNumberOfLines();
		
	}

}

