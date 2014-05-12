package org.visminer.constants;

/**
 * <p>
 * Metrics constants
 * </p>
 * 
 * @author Felipe
 * @version 1.0
 * 
 */
public enum Metrics {

	/**
	 * Lines of Code
	 */
	LOC(1), 
	/**
	 * Number of Methods
	 */
	NOM(2),
	/**
	 * Cyclomatic Complexity
	 */
	CC(3),
	/**
	 * Number of Classes
	 */
	NOC(4),
	/**
	 * Number of Packages
	 */
	NOP(5);
	
	private int value;
	
	private Metrics(int value){
		this.value = value;
	}
	
	/**
	 * 
	 * @return metric id
	 */
	public int getValue(){
		return this.value;
	}
	
	/**
	 * 
	 * @return description about the metric
	 */
	public String getDescription(){
		
		switch(this){
			case LOC: return "Lines of Code";
			case NOM: return "Number of Methods";
			case CC: return "Cyclomatic Complexity";
			case NOC: return "Number of Classes";
			case NOP: return "Number of Packages";
			default: return null;
		}
		
	}
	
}