package org.visminer.metric;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.visminer.constants.Metrics;
import org.visminer.util.DetailAST;

public class NOIMetric implements IMetric{

	private int accumNOI = 0;

	public int calculate(DetailAST ast) {
		
		int classes = 0;
        for(Object object : ast.getRoot().types()){
            TypeDeclaration type = (TypeDeclaration) object;
            if(type.isInterface())
            	classes++;
        }
        accumNOI += classes;
        return classes;
        
	}

	public int getAccumulatedValue() {
		return accumNOI;
	}

	public Metrics getId() {
		return Metrics.NOI;
	}

}
