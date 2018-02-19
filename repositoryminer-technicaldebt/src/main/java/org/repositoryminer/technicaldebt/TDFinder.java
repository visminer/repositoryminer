package org.repositoryminer.technicaldebt;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.repositoryminer.technicaldebt.checker.CPDChecker;
import org.repositoryminer.technicaldebt.checker.CheckStyleChecker;
import org.repositoryminer.technicaldebt.checker.Checker;
import org.repositoryminer.technicaldebt.checker.CodeSmellsChecker;
import org.repositoryminer.technicaldebt.checker.ExCommentChecker;
import org.repositoryminer.technicaldebt.checker.FindBugsCheker;
import org.repositoryminer.technicaldebt.model.TDIndicator;
import org.repositoryminer.technicaldebt.model.TDItem;

public class TDFinder {

	private Set<TDIndicator> tdFilter;
	
	private Checker[] checkers = {
			new CheckStyleChecker(),
			new CodeSmellsChecker(),
			new CPDChecker(),
			new ExCommentChecker(),
			new FindBugsCheker()
	};
	
	public Collection<TDItem> find(String commit, Set<TDIndicator> tdFilter) {
		this.tdFilter = tdFilter;
		Map<String, TDItem> tdItems = new HashMap<String, TDItem>();
		
		for (Checker c : checkers) {
			if (hasAtLeastOneIndicator(c.getIndicators())) {
				c.setTDFilter(tdFilter);
				c.setTDItems(tdItems);
				c.check(commit);
			}
		}
		
		return tdItems.values();
	}

	/*
	 * Checks if the TD indicators filter has at lest one of the indicators passed
	 * as parameter.
	 */
	private boolean hasAtLeastOneIndicator(List<TDIndicator> indicators) {
		boolean result = false;
		for (TDIndicator i : indicators) {
			if (tdFilter.contains(i)) {
				result = true;
				break;
			}
		}
		return result;
	}

}
