package org.repositoryminer.metrics.ast;

import java.util.List;
import org.eclipse.cdt.core.dom.ast.IASTNode;

public class AbstractInclude extends AbstractType {
	
	public List<AbstractInclude> includs;
	
	public String expression;
	public String include;
	


	public AbstractInclude(String include) {
		this.include = include;
	}
	

	public String getInclude() {
		return expression;
	}

	public void setInclude(String include) {
		this.include = include;
	}
	
	public List<AbstractInclude> getIncludes() {
		return includs;
	}

	public void setIncludes(List<AbstractInclude> includes) {
		this.includs = includes;
	}
	

}