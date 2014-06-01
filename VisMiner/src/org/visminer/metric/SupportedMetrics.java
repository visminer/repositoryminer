package org.visminer.metric;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * Divide metrics in code metrics and project metrics 
 * </p>
 * 
 * @author Felipe
 * @version 1.0
 */
public class SupportedMetrics {

	/**
	 * 
	 * @return supported code metrics
	 */
	public static List<IMetric> codeMetrics(){
		
		List<IMetric> metrics = new ArrayList<IMetric>();
		metrics.add(new CCMetric());
		metrics.add(new LOCMetric());
		metrics.add(new NOMMetric());
		
		return metrics;
		
	}
	
	/**
	 * 
	 * @return supported project metrics
	 */
	public static List<IMetric> projectMetrics(){
		
		List<IMetric> metrics = new ArrayList<IMetric>();
		metrics.add(new CCMetric());
		metrics.add(new LOCMetric());
		metrics.add(new NOMMetric());
		metrics.add(new NOPMetric());
		metrics.add(new NOCMetric());
		metrics.add(new NOIMetric());
		
		return metrics;
		
	}
	
}
