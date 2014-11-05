package org.visminer.metric;

public interface IMetric<T> {

	public String getName();
	public String getDescription();
	public T calculate(byte[] data);
	
}
