package org.visminer.metric;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.visminer.constants.Metrics;
import org.visminer.util.DetailAST;

/**
 * <p>
 * Calculates Number of Methods metric
 * </p>
 * 
 * @author Felipe
 * @version 1.0
 */
public class NOMMetric implements IMetric{

	private int accumNOM = 0;
	
	public Metrics getId(){
		return Metrics.NOM;
	}
	
	public int calculate(DetailAST ast){
		
		int methodCounter = 0;
		
		for(int i = 0; i < ast.getRoot().types().size(); i++){
			TypeDeclaration type = (TypeDeclaration) ast.getRoot().types().get(i);
			methodCounter += type.getMethods().length;
		}
		
		accumNOM += methodCounter;
		return methodCounter;
		
	}

	public int getAccumulatedValue() {
		return accumNOM;
	}
	
}
