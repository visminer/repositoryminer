package org.visminer.metric;

import org.eclipse.jface.text.Document;
import org.visminer.constants.Metrics;
import org.visminer.util.DetailAST;

/**
 * <p>
 * Calculates Lines of Code metric
 * </p>
 * 
 * @author Felipe
 * @version 1.0
 */
public class LOCMetric implements IMetric{

	private int accumLOC = 0;
	
	public Metrics getId(){
		return Metrics.LOC;
	}
	
	public int calculate(DetailAST ast){
		
		Document doc = new Document(ast.getSource());
		accumLOC += doc.getNumberOfLines();
		return doc.getNumberOfLines();
		
	}

	@Override
	public int getAccumulatedValue() {
		return accumLOC;
	}

}

