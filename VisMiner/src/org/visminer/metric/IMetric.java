package org.visminer.metric;

import org.visminer.constants.Metrics;
import org.visminer.util.DetailAST;

/**
 * <p>
 * Java interface for metrics
 * </p>
 * 
 * @author Felipe
 * @version 1.0
 */
public interface IMetric {
	
	/**
	 * 
	 * @param ast : source code AST
	 * @return metric value calculated
	 */
	public int calculate(DetailAST ast);
	
	/**
	 * <p>
	 * you can create a propriety to accumulate metrics values and use this method to return this propriety
	 * </p>
	 * 
	 * @return metric value accumulated
	 */
	public int getAccumulatedValue();
	
	/**
	 * 
	 * @return metric constant id
	 */
	public Metrics getId();
	
}
