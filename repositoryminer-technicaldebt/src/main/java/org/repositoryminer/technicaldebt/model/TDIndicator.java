package org.repositoryminer.technicaldebt.model;

import static org.repositoryminer.technicaldebt.model.TDType.CODE_DEBT;
import static org.repositoryminer.technicaldebt.model.TDType.DESIGN_DEBT;

import java.util.Arrays;
import java.util.List;

public enum TDIndicator {

	// Code Smells
	GOD_CLASS(CODE_DEBT, DESIGN_DEBT),
	COMPLEX_METHOD(CODE_DEBT, DESIGN_DEBT),
	FEATURE_ENVY(CODE_DEBT, DESIGN_DEBT),
	BRAIN_METHOD(CODE_DEBT, DESIGN_DEBT),
	DATA_CLASS(CODE_DEBT, DESIGN_DEBT),
	
	// PMD/CPD
	DUPLICATED_CODE(CODE_DEBT, DESIGN_DEBT),
	
	// CheckStyle
	CODE_WITHOUT_STANDARDS(CODE_DEBT),
	
	// FindBugs
	AUTOMATIC_STATIC_ANALYSIS_ISSUES(CODE_DEBT, DESIGN_DEBT),
	MULTITHREAD_CORRECTNESS(CODE_DEBT),
	SLOW_ALGORITHM(CODE_DEBT);

	private TDType types[];
	
	TDIndicator(TDType... types) {
		this.types = types;
	}
	
	public static TDIndicator getTDIndicator(String indicatorName) {
		for (TDIndicator indicator : TDIndicator.values()) {
			if (indicator.toString().equals(indicatorName)) {
				return indicator;
			}
		}
		return null;
	}
	
	public List<TDType> getTypes() {
		return Arrays.asList(types);
	}
	
}
