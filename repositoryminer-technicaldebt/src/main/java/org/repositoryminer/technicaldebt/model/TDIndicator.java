package org.repositoryminer.technicaldebt.model;

import static org.repositoryminer.technicaldebt.model.TDType.*;

import java.util.ArrayList;
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
	SLOW_ALGORITHM(CODE_DEBT),

	// eXcomment
	COMMENT_ANALYSIS_UNKNOWN_DEBT(UNKNOWN_DEBT),
	COMMENT_ANALYSIS_CODE_DEBT(CODE_DEBT),
	COMMENT_ANALYSIS_DESIGN_DEBT(DESIGN_DEBT),
	COMMENT_ANALYSIS_ARCHITECTURE_DEBT(ARCHITECTURE_DEBT),
	COMMENT_ANALYSIS_BUILD_DEBT(BUILD_DEBT),
	COMMENT_ANALYSIS_DEFECT_DEBT(DEFECT_DEBT),
	COMMENT_ANALYSIS_DOCUMENTATION_DEBT(DOCUMENTATION_DEBT),
	COMMENT_ANALYSIS_PEOPLE_DEBT(PEOPLE_DEBT),
	COMMENT_ANALYSIS_REQUIREMENT_DEBT(REQUIREMENT_DEBT), 
	COMMENT_ANALYSIS_TEST_DEBT(TEST_DEBT);
	
	private List<TDType> types;
	
	TDIndicator(TDType... types) {
		this.types = Arrays.asList(types);
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
		return types;
	}
	
	public static List<TDIndicator> getByType(TDType type) {
		List<TDIndicator> result = new ArrayList<TDIndicator>();
		
		if (type == null) {
			return result;
		}
		
		for (TDIndicator indicator : TDIndicator.values()) {
			if (indicator.types.contains(type)) {
				result.add(indicator);
			}
		}
		
		return result;
	}
	
}
