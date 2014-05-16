package org.visminer.metric;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.visminer.constants.Metrics;
import org.visminer.util.DetailAST;


public class NOCMetric implements IMetric{

	private int accumNOC = 0;

	@Override
	public int calculate(DetailAST ast) {
		
		int classes = 0;
        for(Object object : ast.getRoot().types()){
            TypeDeclaration type = (TypeDeclaration) object;
            if(!type.isInterface())
            	classes++;
        }
        accumNOC += classes;
        return classes;
        
	}

	@Override
	public int getAccumulatedValue() {
		return accumNOC;
	}

	@Override
	public Metrics getId() {
		return Metrics.NOC;
	}
	
}
