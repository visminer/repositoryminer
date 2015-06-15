package br.edu.ufba.softvis.visminer.constant;

public enum SoftwareUnitType {

	PROJECT(1),
	PACKAGE(2),
	FILE(3),
	CLASS(4),
	METHOD(5);
	
	private int id;
	
	private SoftwareUnitType(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
	public static SoftwareUnitType parse(int id){
		
		for(SoftwareUnitType softwareUnitType : SoftwareUnitType.values()){
			if(softwareUnitType.getId() == id){
				return softwareUnitType;
			}
		}
		
		throw new IllegalArgumentException("No software unit type found for id "+id);
		
	}
	
}
