package org.repositoryminer.technicaldebt.model;

import static org.repositoryminer.technicaldebt.model.TDIndicator.*;

public enum TDType {

	CODE_DEBT(GOD_CLASS, COMPLEX_METHOD, FEATURE_ENVY, DUPLICATED_CODE, BRAIN_METHOD, AUTOMATIC_STATIC_ANALYSIS_ISSUES,
			DATA_CLASS, MULTITHREAD_CORRECTNESS, CODE_WITHOUT_STANDARDS, SLOW_ALGORITHM),
	DESIGN_DEBT(GOD_CLASS, COMPLEX_METHOD, FEATURE_ENVY, DUPLICATED_CODE, BRAIN_METHOD,
			AUTOMATIC_STATIC_ANALYSIS_ISSUES, DATA_CLASS);

	private TDIndicator[] indicators;

	private TDType(TDIndicator... indicators) {
		this.indicators = indicators;
	}

	public TDIndicator[] getIndicators() {
		return indicators;
	}

}