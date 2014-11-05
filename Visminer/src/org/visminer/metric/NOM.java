package org.visminer.metric;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.visminer.utility.DetailAST;

public class NOM implements IMetric<Integer>{

	private final String NAME = "NOM";
	private final String DESCRIPTION = "This metric calculates number of methods";
	
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
		
		int methodCounter = 0;
		
		String content = new String(data);
		
		DetailAST ast = new DetailAST();
		ast.parserFromString(content);
		
		for(int i = 0; i < ast.getRoot().types().size(); i++){
			TypeDeclaration type = (TypeDeclaration) ast.getRoot().types().get(i);
			methodCounter += type.getMethods().length;
		}
		
		return methodCounter;
		
	}

}
