package org.visminer.metric;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.visminer.constants.Metrics;
import org.visminer.util.DetailAST;

public class NOMMetric implements IMetric{

	public Metrics getId(){
		return Metrics.NOM;
	}
	
	public int calculate(DetailAST ast){
		
		int methodCounter = 0;
		
		for(int i = 0; i < ast.getRoot().types().size(); i++){
			TypeDeclaration type = (TypeDeclaration) ast.getRoot().types().get(i);
			methodCounter += type.getMethods().length;
		}
		
		return methodCounter;
		
	}
	
}
