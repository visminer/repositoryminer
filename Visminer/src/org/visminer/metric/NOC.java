package org.visminer.metric;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.visminer.utility.DetailAST;

public class NOC  implements IMetric<Integer>{

	private final String NAME = "NOC";
	private final String DESCRIPTION = "This metric calculates number of lines in code";
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public Integer calculate(byte[] data) {
		
		String content = new String(data);
		
		DetailAST ast = new DetailAST();
		ast.parserFromString(content);
		
		int classes = 0;
        for(Object object : ast.getRoot().types()){
            TypeDeclaration type = (TypeDeclaration) object;
            if(!type.isInterface())
            	classes++;
        }
        
        return classes;
		
	}

}
