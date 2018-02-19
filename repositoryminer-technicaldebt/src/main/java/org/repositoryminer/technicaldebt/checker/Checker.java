package org.repositoryminer.technicaldebt.checker;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.repositoryminer.technicaldebt.model.TDIndicator;
import org.repositoryminer.technicaldebt.model.TDItem;

/**
 * This class is the base TD checker. Extends this class to implement your
 * checker. The list of TD items is shred between all the checkers. Use the
 * methods offered by this checker to find the TD item associated with a file
 * and to add occurrences of a certain TD indicator. Only the indicators
 * filtered by the user will be accepted.
 */
public abstract class Checker {

	private Map<String, TDItem> tdItems;
	private Set<TDIndicator> tdFilter;

	/**
	 * Finds the TD occurrences in a version.
	 * 
	 * @param commit
	 */
	public abstract void check(String commit);

	/**
	 * @return the TD indicators supported by the checker.
	 */
	public abstract List<TDIndicator> getIndicators();

	public void setTDItems(Map<String, TDItem> tdItems) {
		this.tdItems = tdItems;
	}
	
	public void setTDFilter(Set<TDIndicator> tdFilter) {
		this.tdFilter = tdFilter;
	}
	
	/**
	 * Finds the TD item instance associated with a file.
	 * 
	 * @param filename
	 * @return the TD item
	 */
	protected TDItem searchFile(String filename) {
		TDItem tdItem = tdItems.get(filename);
		if (tdItem == null) {
			tdItem = new TDItem(filename);
			tdItems.put(filename, tdItem);
		}
		return tdItem;
	}

	/*
	 * Checks if the TD indicator is accepted by the filter. If the indicator is
	 * accepted by the filter its occurrences are added to the item.
	 */
	protected void addTDIndicator(TDItem tdItem, TDIndicator indicator, int occurrences) {
		if (indicator == null || !tdFilter.contains(indicator)) {
			return;
		}
		tdItem.addToIndicator(indicator, occurrences);
	}

}
