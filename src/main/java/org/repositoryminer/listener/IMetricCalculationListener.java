package org.repositoryminer.listener;

import java.util.Map;

public interface IMetricCalculationListener {

	public void updateMetricValue(String metricName, int value);
	
	public void updateMetricValue(String metricName, float value);
	
	public void updateMethodBasedMetricValue(String metricName, int accumulatedValue, 
			Map<String, Integer> valuesPerMethod);
	
}
