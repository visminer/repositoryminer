package org.visminer.constants;

public enum Metrics {

	LOC(1), NOM(2), CC(3), NOC(4), NOP(5);
	
	private int value;
	
	private Metrics(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
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