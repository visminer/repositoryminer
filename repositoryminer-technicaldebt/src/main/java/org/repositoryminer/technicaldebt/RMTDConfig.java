package org.repositoryminer.technicaldebt;

import java.util.Set;

import org.repositoryminer.technicaldebt.model.TDIndicator;

public class RMTDConfig {

	private String reference;
	private Set<TDIndicator> indicators;
	
	public RMTDConfig(String reference, Set<TDIndicator> indicators) {
		this.reference = reference;
		this.indicators = indicators;
	}

	public String getReference() {
		return reference;
	}
	
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public Set<TDIndicator> getIndicators() {
		return indicators;
	}
	
	public void setIndicators(Set<TDIndicator> indicators) {
		this.indicators = indicators;
	}

	public boolean isValid() {
		return (reference != null && reference.length() > 0) && (indicators != null && indicators.size() > 0);
	}
	
}
