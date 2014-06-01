package org.visminer.metric;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.visminer.constants.Metrics;
import org.visminer.util.DetailAST;

/**
 * <p>
 * Calculates Number of Interfaces metric
 * </p>
 * 
 * @author FabioRosario
 * @version 1.0
 */
public class NOIMetric implements IMetric{

	private int accumNOI = 0;

	public int calculate(DetailAST ast) {
		
		int interfaces = 0;
        for(Object object : ast.getRoot().types()){
            TypeDeclaration type = (TypeDeclaration) object;
            if(! type.isInterface())
            	interfaces++;
        }
        accumNOI += interfaces;
        return interfaces;
        
	}

	public int getAccumulatedValue() {
		return accumNOI;
	}

	public Metrics getId() {
		return Metrics.NOI;
	}

}
