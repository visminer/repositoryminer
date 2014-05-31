package org.visminer.metric;

import java.util.ArrayList;
import java.util.List;

public class SupportedMetrics {

	public static List<IMetric> codeMetrics(){
		
		List<IMetric> metrics = new ArrayList<IMetric>();
		metrics.add(new CCMetric());
		metrics.add(new LOCMetric());
		metrics.add(new NOMMetric());
		
		return metrics;
		
	}
	
	public static List<IMetric> projectMetrics(){
		
		List<IMetric> metrics = new ArrayList<IMetric>();
		metrics.add(new CCMetric());
		metrics.add(new LOCMetric());
		metrics.add(new NOMMetric());
		metrics.add(new NOPMetric());
		metrics.add(new NOCMetric());
		
		return metrics;
		
	}
	
}
