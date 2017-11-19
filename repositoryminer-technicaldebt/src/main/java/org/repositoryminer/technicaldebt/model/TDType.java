package org.repositoryminer.technicaldebt.model;

import static org.repositoryminer.technicaldebt.model.TDIndicator.*;

import java.util.Arrays;
import java.util.List;

public enum TDType {

	CODE_DEBT(GOD_CLASS, COMPLEX_METHOD, FEATURE_ENVY, DUPLICATED_CODE, BRAIN_METHOD, AUTOMATIC_STATIC_ANALYSIS_ISSUES,
			DATA_CLASS, MULTITHREAD_CORRECTNESS, CODE_WITHOUT_STANDARDS, SLOW_ALGORITHM),
	DESIGN_DEBT(GOD_CLASS, COMPLEX_METHOD, FEATURE_ENVY, DUPLICATED_CODE, BRAIN_METHOD,
			AUTOMATIC_STATIC_ANALYSIS_ISSUES, DATA_CLASS);

	private TDIndicator[] indicators;

	private TDType(TDIndicator... indicators) {
		this.indicators = indicators;
	}

	public List<TDIndicator> getIndicators() {
		return Arrays.asList(indicators);
	}

}