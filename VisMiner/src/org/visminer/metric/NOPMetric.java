package org.visminer.metric;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.visminer.constants.Metrics;
import org.visminer.util.DetailAST;

public class NOPMetric implements IMetric{

	private Set<String> packges = new HashSet<String>();

	@Override
	public int calculate(DetailAST ast) {
		
		PackageDeclaration pd = ast.getRoot().getPackage();
		if(pd != null){
			packges.add(pd.getName().getFullyQualifiedName());
			return 1;
		}
		
		return 0;
		
	}

	@Override
	public int getAccumulatedValue() {
		return packges.size();
	}

	@Override
	public Metrics getId() {
		return Metrics.NOP;
	}
	
}
