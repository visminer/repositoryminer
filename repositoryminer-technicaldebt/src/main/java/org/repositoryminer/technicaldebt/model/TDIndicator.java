package org.repositoryminer.technicaldebt.model;

import static org.repositoryminer.technicaldebt.model.TDType.*;

public enum TDIndicator {

	GOD_CLASS(CODE_DEBT, DESIGN_DEBT),
	COMPLEX_METHOD(CODE_DEBT, DESIGN_DEBT),
	FEATURE_ENVY(CODE_DEBT, DESIGN_DEBT),
	DUPLICATED_CODE(CODE_DEBT, DESIGN_DEBT),
	BRAIN_METHOD(CODE_DEBT, DESIGN_DEBT),
	AUTOMATIC_STATIC_ANALYSIS_ISSUES(CODE_DEBT, DESIGN_DEBT),
	DATA_CLASS(CODE_DEBT, DESIGN_DEBT),
	MULTITHREAD_CORRECTNESS(CODE_DEBT),
	CODE_WITHOUT_STANDARDS(CODE_DEBT),
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
	
	public TDType[] getTypes() {
		return types;
	}
}