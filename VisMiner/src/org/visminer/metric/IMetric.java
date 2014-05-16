package org.visminer.metric;

import org.visminer.constants.Metrics;
import org.visminer.util.DetailAST;

public interface IMetric {

	public int calculate(DetailAST ast);
	public int getAccumulatedValue();
	public Metrics getId();
	
}
