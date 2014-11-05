package org.visminer.config;

import java.util.List;

import org.visminer.metric.IMetric;

public class MetricAttribute {

	private IMetric<?> metricCls;
	private List<String> extension;
	private List<String> mimeType;
	
	public MetricAttribute(IMetric<?> metricCls, List<String> extension,
			List<String> mimeType) {
		super();
		this.metricCls = metricCls;
		this.extension = extension;
		this.mimeType = mimeType;
	}
	
	public IMetric<?> getMetricCls() {
		return metricCls;
	}
	public void setMetricCls(IMetric<?> metricCls) {
		this.metricCls = metricCls;
	}
	public List<String> getExtension() {
		return extension;
	}
	public void setExtension(List<String> extension) {
		this.extension = extension;
	}
	public List<String> getMimeType() {
		return mimeType;
	}
	public void setMimeType(List<String> mimeType) {
		this.mimeType = mimeType;
	}
	
}
